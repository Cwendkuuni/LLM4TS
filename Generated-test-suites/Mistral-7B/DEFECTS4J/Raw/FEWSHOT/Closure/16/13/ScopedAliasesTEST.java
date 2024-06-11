package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AliasTransformationHandler;
import com.google.javascript.jscomp.CompilationTestBase;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.SourcePosition;

@RunWith(Parameterized.class)
public class ScopedAliasesTest extends CompilationTestBase {

  private static final String CLASS_NAME = "ScopedAliases";
  private static final String TARGET_CLASS_NAME = "com.google.javascript.jscomp.ScopedAliases";
  private AbstractCompiler compiler;
  private AliasTransformationHandler transformationHandler;

  public ScopedAliasesTest(String input, String expectedOutput) {
    super(input, expectedOutput);
  }

  @Parameterized.Parameters(name = "{0}")
  public static List<Object[]> data() {
    return asList(new Object[][] {
        {
            "testScopedAliases1.js",
            "testScopedAliases1_out.js"
        },
        {
            "testScopedAliases2.js",
            "testScopedAliases2_out.js"
        },
        {
            "testScopedAliases3.js",
            "testScopedAliases3_out.js"
        },
        {
            "testScopedAliases4.js",
            "testScopedAliases4_out.js"
        },
        {
            "testScopedAliases5.js",
            "testScopedAliases5_out.js"
        },
        {
            "testScopedAliases6.js",
            "testScopedAliases6_out.js"
        },
        {
            "testScopedAliases7.js",
            "testScopedAliases7_out.js"
        },
        {
            "testScopedAliases8.js",
            "testScopedAliases8_out.js"
        },
        {
            "testScopedAliases9.js",
            "testScopedAliases9_out.js"
        },
        {
            "testScopedAliases10.js",
            "testScopedAliases10_out.js"
        },
        {
            "testScopedAliases11.js",
            "testScopedAliases11_out.js"
        },
        {
            "testScopedAliases12.js",
            "testScopedAliases12_out.js"
        },
        {
            "testScopedAliases13.js",
            "testScopedAliases13_out.js"
        },
        {
            "testScopedAliases14.js",
            "testScopedAliases14_out.js"
        },
        {
            "testScopedAliases15.js",
            "testScopedAliases15_out.js"
        },
        {
            "testScopedAliases16.js",
            "testScopedAliases16_out.js"
        },
        {
            "testScopedAliases17.js",
            "testScopedAliases17_out.js"
        },
        {
            "testScopedAliases18.js",
            "testScopedAliases18_out.js"
        },
        {
            "testScopedAliases19.js",
            "testScopedAliases19_out.js"
        },
        {
            "testScopedAliases20.js",
            "testScopedAliases20_out.js"
        }
    });
  }

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    compiler = getCompileTestSetup(new String[] { TARGET_CLASS_NAME });
    transformationHandler = new AliasTransformationHandler();
  }

  @Before
  public void setUp() throws Exception {
    compiler = (AbstractCompiler) getCompilationTestSetup(new String[] { TARGET_CLASS_NAME });
    compiler.addPass(new HotSwapCompilerPass(
        new ScopedAliases(compiler, null, transformationHandler)));
  }

  @After
  public void tearDown() throws Exception {
    compiler = null;
  }

  @Test
  public void testScopedAliases() throws Exception {
    compileAndCheckOutput(compiler, getSourceForTest(getInputName()));
  }

  @Test
  public void testAliasesUsages() throws Exception {
    String input = getSourceForTest(getInputName());
    Node root = parse(input);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    List<AliasUsage> aliasUsages = traversal.getAliasUsages();

    for (AliasUsage aliasUsage : aliasUsages) {
      if (aliasUsage instanceof AliasedNode) {
        AliasedNode aliasedNode = (AliasedNode) aliasUsage;
        Node aliasReference = aliasedNode.aliasReference;
        Node aliasDefinition = aliasedNode.aliasDefinition;

        assertEquals(aliasDefinition.getQualifiedName(),
            aliasReference.getQualifiedName());
      } else if (aliasUsage instanceof AliasedTypeNode) {
        AliasedTypeNode aliasedTypeNode = (AliasedTypeNode) aliasUsage;
        Node typeReference = aliasedTypeNode.typeReference;
        String aliasName = aliasedTypeNode.aliasName;

        assertEquals(aliasName, typeReference.getString());
      }
    }
  }

  @Test
  public void testNoAliases() throws Exception {
    String input = getSourceForTest("testNoAliases.js");
    Node root = parse(input);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    assertTrue(aliasUsages.isEmpty());
  }

  @Test
  public void testProperlyDefinedScopedFunction() throws Exception {
    String input = getSourceForTest("testProperlyDefinedScopedFunction.js");
    Node root = parse(input);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    // There should be no errors and no alias usages.
    assertFalse(traversal.hasErrors());
    assertTrue(traversal.getAliasUsages().isEmpty());

    // The code should be transformed correctly.
    String expectedOutput = getExpectedOutputForTest(getInputName() + "_out.js");
    String output = getCompilableSource(compileAndCheckOutput(compiler, input));

    assertEquals(expectedOutput, output);
  }

  @Test
  public void testImproperlyDefinedScopedFunction() throws Exception {
    String input = getSourceForTest("testImproperlyDefinedScopedFunction.js");
    Node root = parse(input);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    // There should be errors.
    assertTrue(traversal.hasErrors());

    // The code should not be transformed.
    String output = getCompilableSource(compileAndCheckOutput(compiler, input));
    assertEquals(input, output);
  }

  @Test
  public void testRefersToThis() throws Exception {
    String input = getSourceForTest("testRefersToThis.js");
    Node root = parse(input);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    // There should be an error.
    assertTrue(traversal.hasErrors());

    // The code should not be transformed.
    String output = getCompilableSource(compileAndCheckOutput(compiler, input));
    assertEquals(input, output);
  }

  @Test
  public void testUsesReturn() throws Exception {
    String input = getSourceForTest("testUsesReturn.js");
    Node root = parse(input);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    // There should be an error.
    assertTrue(traversal.hasErrors());

    // The code should not be transformed.
    String output = getCompilableSource(compileAndCheckOutput(compiler, input));
    assertEquals(input, output);
  }

  @Test
  public void testUsesThrow() throws Exception {
    String input = getSourceForTest("testUsesThrow.js");
    Node root = parse(input);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    // There should be an error.
    assertTrue(traversal.hasErrors());

    // The code should not be transformed.
    String output = getCompilableSource(compileAndCheckOutput(compiler, input));
    assertEquals(input, output);
  }

  @Test
  public void testAliasRedefined() throws Exception {
    String input = getSourceForTest("testAliasRedefined.js");
    Node root = parse(input);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    // There should be an error.
    assertTrue(traversal.hasErrors());

    // The code should not be transformed.
    String output = getCompilableSource(compileAndCheckOutput(compiler, input));
    assertEquals(input, output);
  }
}