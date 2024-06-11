package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AnalyzePrototypeProperties.NameInfo;
import com.google.javascript.jscomp.AnalyzePrototypeProperties.Property;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassTest;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.IdGenerator;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.IR;

public class CrossModuleMethodMotionTest extends CompilerPassTest {

  private static final String TEST_FILE = "test_cross_module_method_motion.js";

  @Override
  protected CompilerPass getProcessorUnderTest() {
    CompilerOptions options = new CompilerOptions(emptyOptions());
    IdGenerator idGenerator = new IdGenerator();
    return new CrossModuleMethodMotion(getCompiler(), idGenerator, true);
  }

  @Test
  public void testMoveMethods() {
    SourceFile source = compileTestFile(TEST_FILE);
    JSModule deepestCommonModule = getModuleWithName("module1");

    runPasses(source, getProcessorUnderTest());

    // Move methods from module1 to module2
    for (NameInfo nameInfo : getProcessorUnderTest().analyzer.getAllNameInfo()) {
      if (nameInfo.getDeepestCommonModuleRef() == deepestCommonModule && nameInfo.isReferenced()) {
        Property property = nameInfo.getDeclarations().iterator().next();
        if (property != null && property.getValue() != null && property.getValue().isFunction()) {
          assertFalse(property.getValue().getParent().isGetterDef());
          assertFalse(property.getValue().getParent().isSetterDef());

          Node proto = property.getPrototype();
          Node prototypeProperty = IR.getprop(proto, IR.string(nameInfo.name));
          Node stubCall = (Node) prototypeProperty.getChild(0);
          assertSame(STUB_METHOD_NAME, stubCall.getChild(0).getString());
          Node unstubCall = (Node) prototypeProperty.getChild(1);
          assertSame(UNSTUB_METHOD_NAME, unstubCall.getChild(0).getString());

          JSModule deeperModule = getModuleWithName("module2");
          Node unstubFunction = getFunctionWithName(deeperModule, nameInfo.name);
          assertSame(unstubCall.getChild(1), unstubFunction);

          Node stubFunction = getFunctionWithName(deepestCommonModule, nameInfo.name);
          assertSame(stubCall.getChild(1), stubFunction);
        }
      }
    }
  }

  @Test
  public void testNoMove() {
    SourceFile source = compileTestFile(TEST_FILE);
    runPasses(source, getProcessorUnderTest());

    // No method moved from module1 to module2
    for (NameInfo nameInfo : getProcessorUnderTest().analyzer.getAllNameInfo()) {
      if (nameInfo.getDeepestCommonModuleRef() == null || nameInfo.getDeepestCommonModuleRef() == getModuleWithName("module1")) {
        continue;
      }
      Property property = nameInfo.getDeclarations().iterator().next();
      if (property != null && property.getValue() != null && property.getValue().isFunction()) {
        assertNull(getFunctionWithName(getModuleWithName("module2"), nameInfo.name));
      }
    }
  }

  @Test
  public void testNoStubGeneration() {
    SourceFile source = compileTestFile(TEST_FILE);
    JSModule deepestCommonModule = getModuleWithName("module1");

    // Disable method stubbing
    getProcessorUnderTest().idGenerator = new IdGenerator() {
      @Override
      public boolean hasGeneratedAnyIds() {
        return false;
      }
    };

    runPasses(source, getProcessorUnderTest());

    // No method moved or stubbed from module1 to module2
    for (NameInfo nameInfo : getProcessorUnderTest().analyzer.getAllNameInfo()) {
      if (nameInfo.getDeepestCommonModuleRef() == deepestCommonModule && nameInfo.isReferenced()) {
        Property property = nameInfo.getDeclarations().iterator().next();
        if (property != null && property.getValue() != null && property.getValue().isFunction()) {
          assertNull(getFunctionWithName(getModuleWithName("module2"), nameInfo.name));
          assertNull(getFunctionWithName(deepestCommonModule, STUB_METHOD_NAME));
        }
      }
    }
  }
}