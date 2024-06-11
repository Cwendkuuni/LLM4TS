package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AnalyzePrototypeProperties.NameInfo;
import com.google.javascript.jscomp.AnalyzePrototypeProperties.Property;
import com.google.javascript.jscomp.AnalyzePrototypeProperties.Symbol;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CrossModuleMethodMotionTest {
  Compiler compiler;
  AbstractCompiler abstractCompiler;
  IdGenerator idGenerator;
  JSModuleGraph moduleGraph;
  CrossModuleMethodMotion pass;

  @Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions();
    abstractCompiler = Mockito.mock(AbstractCompiler.class);
    compiler = new Compiler(options, null, null, null);
    compiler.setCompilerPassManager(new CompilerPassManager());
    idGenerator = new IdGenerator();
    moduleGraph = Mockito.mock(JSModuleGraph.class);
    pass = new CrossModuleMethodMotion(abstractCompiler, idGenerator, true);
  }

  @Test
  public void testMoveMethods() {
    // Given: An analyzer with some name infos
    NameInfo nameInfo1 = new NameInfo("A", null, null, null, null, null, null);
    NameInfo nameInfo2 = new NameInfo("B", null, null, null, null, null, null);
    NameInfo nameInfo3 = new NameInfo("C", null, null, null, null, null, null);
    NameInfo nameInfo4 = new NameInfo("D", null, null, null, null, null, null);
    Collection<NameInfo> allNameInfo = Mockito.mock(Collection.class);
    Mockito.when(allNameInfo.contains(Mockito.any(NameInfo.class))).thenReturn(true);
    Mockito.when(allNameInfo.iterator()).thenReturn(Mockito.mock(Iterator.class));

    // When: The pass moves methods
    pass.moveMethods(allNameInfo);

    // Then: The methods have been moved
    // TODO: Write test cases for specific method movement scenarios
  }

  @Test
  public void testIdGenerator() {
    // Given: An id generator
    IdGenerator idGenerator = new IdGenerator();

    // When: A new id is generated
    int newId = idGenerator.newId();

    // Then: The id is greater than 0
    assertThat(newId).isGreaterThan(0);
  }
}