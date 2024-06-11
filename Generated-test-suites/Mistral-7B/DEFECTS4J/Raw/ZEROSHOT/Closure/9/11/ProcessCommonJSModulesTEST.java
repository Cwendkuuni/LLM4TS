import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceName;

import java.io.PrintStream;
import java.util.Set;

public class ProcessCommonJSModulesTest {

  private CompilerInput createCompilerInput(String fileName) {
    String code = "function main() {\n" +
        "require('module1');\n" +
        "}\n";
    return new CompilerInput(new SourceName("test.js"), code);
  }

  @org.junit.Test
  public void testProcessCommonJSModules() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CompilerOptions options = new CompilerOptions();
    options.setCompilerPasses(Sets.newHashSet(new CompilerPass[] {
      new ProcessCommonJSModules(compiler, ".", false)
    }));

    CompilerInput input = createCompilerInput("test.js");

    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, ".", false);
    pass.process(input, null);

    assertNotNull(pass.getModule());
    assertEquals("module1", pass.getModule().getName());
  }

  @org.junit.Test
  public void testProcessCommonJSModulesWithExports() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CompilerOptions options = new CompilerOptions();
    options.setCompilerPasses(Sets.newHashSet(new CompilerPass[] {
      new ProcessCommonJSModules(compiler, ".", true)
    }));

    CompilerInput input = createCompilerInput("module1.js");
    input.setSourceFileName("module1.js");

    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, ".", true);
    pass.process(input, null);

    JSModule module = pass.getModule();
    assertNotNull(module);
    assertEquals("module1", module.getName());

    Node exports = module.getExport();
    assertNotNull(exports);
    assertEquals("exports", exports.getString());

    PrintStream errorStream = mock(PrintStream.class);
    when(compiler.getErrorStream()).thenReturn(errorStream);

    input = createCompilerInput("test.js");
    input.setSourceFileName("test.js");
    input.addProvide("module1");

    pass = new ProcessCommonJSModules(compiler, ".", true);
    pass.process(input, null);

    assertEquals("", errorStream.getErrorOutput());
  }
}