import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.abstractPostOrder;
import static com.google.javascript.rhino.NodeFactory.createVariable;
import static com.google.javascript.rhino.NodeFactory.createIdentifier;
import static com.google.javascript.rhino.NodeFactory.createAssign;
import static com.google.javascript.rhino.NodeFactory.createIfStatement;
import static com.google.javascript.rhino.NodeFactory.createBlock;
import static com.google.javascript.rhino.NodeFactory.createStatement;
import static com.google.javascript.rhino.NodeFactory.createBooleanLiteral;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.NodeFactory;

public class CollapseVariableDeclarationsTest {

  @Test
  public void testCollapsingSimpleDeclarations() {
    Compiler compiler = new Compiler();
    CompilerOptions options = CompilerOptions.defaultOptions();
    compiler.setOptions(options);

    Node root = NodeFactory.createProgram();
    Node variables = NodeFactory.createBlock(Arrays.asList(
        createVariable(createIdentifier("a")),
        createVariable(createIdentifier("b")),
        createVariable(createIdentifier("c"))
    ));
    root.setBody(variables);

    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);

    Node expectedResult = NodeFactory.createBlock(ImmutableList.of(
        createVariable(createIdentifier("a"), createIdentifier("b"), createIdentifier("c"))
    ));

    assertEquals(expectedResult.toSource(), root.toSource());
  }

  @Test
  public void testCollapsingAssignments() {
    Compiler compiler = new Compiler();
    CompilerOptions options = CompilerOptions.defaultOptions();
    compiler.setOptions(options);

    Node root = NodeFactory.createProgram();
    Node assignments = NodeFactory.createBlock(Arrays.asList(
        createAssign(createVariable(createIdentifier("a")), createBooleanLiteral(true)),
        createAssign(createVariable(createIdentifier("b")), createBooleanLiteral(true)),
        createAssign(createVariable(createIdentifier("c")), createBooleanLiteral(true))
    ));
    root.setBody(assignments);

    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);

    Node expectedResult = NodeFactory.createBlock(ImmutableList.of(
        createAssign(createVariable(createIdentifier("c")), createBooleanLiteral(true), createBooleanLiteral(true), createBooleanLiteral(true))
    ));

    assertEquals(expectedResult.toSource(), root.toSource());
  }

  @Test
  public void testCollapsingNestedDeclarations() {
    Compiler compiler = new Compiler();
    CompilerOptions options = CompilerOptions.defaultOptions();
    compiler.setOptions(options);

    Node root = NodeFactory.createProgram();
    Node function = NodeFactory.createFunction(
        NodeFactory.createIdentifier("test"),
        NodeFactory.createBlock(Arrays.asList(
            NodeFactory.createVariable(createIdentifier("x")),
            NodeFactory.createBlock(Arrays.asList(
                NodeFactory.createVariable(createIdentifier("a")),
                NodeFactory.createVariable(createIdentifier("b")),
                NodeFactory.createVariable(createIdentifier("c"))
            )),
            NodeFactory.createReturnStatement(NodeFactory.createIdentifier("x"))
        ))
    );
    root.setBody(function);

    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);

    Node expectedResult = NodeFactory.createFunction(
        NodeFactory.createIdentifier("test"),
        NodeFactory.createBlock(Arrays.asList(
            NodeFactory.createVariable(createIdentifier("x"), NodeFactory.createBlock(
                Arrays.asList(
                    createVariable(createIdentifier("a"), createIdentifier("b"), createIdentifier("c"))
                )
            ))
        ))
    );

    assertEquals(expectedResult.toSource(), root.toSource());
  }

  @Test
  public void testCollapsingEmptyBlock() {
    Compiler compiler = new Compiler();
    CompilerOptions options = CompilerOptions.defaultOptions();
    compiler.setOptions(options);

    Node root = NodeFactory.createProgram();
    Node variables = NodeFactory.createBlock();
    root.setBody(variables);

    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);

    Node expectedResult = NodeFactory.createProgram();

    assertEquals(expectedResult.toSource(), root.toSource());
  }

  @Test
  public void testCollapsingWithException() {
    Compiler compiler = new Compiler();
    CompilerOptions options = CompilerOptions.defaultOptions();
    compiler.setOptions(options);

    Node root = NodeFactory.createProgram();
    Node variables = NodeFactory.createBlock(Arrays.asList(
        NodeFactory.createVariable(createIdentifier("a")),
        NodeFactory.createVariable(createIdentifier("b")),
        NodeFactory.createThrowStatement(NodeFactory.createError("Test error"))
    ));
    root.setBody(variables);

    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    try {
      pass.process(null, root);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }

    root = NodeFactory.createProgram();
    variables = NodeFactory.createBlock(Arrays.asList(
        NodeFactory.createVariable(createIdentifier("a")),
        NodeFactory.createVariable(createIdentifier("b")),
        NodeFactory.createIfStatement(
            NodeFactory.createBinaryOperator(
                NodeFactory.createIdentifier("x"),
                Token.EQ,
                NodeFactory.createBooleanLiteral(false)
            ),
            NodeFactory.createBlock(Arrays.asList(
                NodeFactory.createVariable(createIdentifier("c"))
            )),
            NodeFactory.createBlock()
        )
    ));
    root.setBody(variables);

    try {
      pass.process(null, root);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testCollapsingWithVariableInConditional() {
    Compiler compiler = new Compiler();
    CompilerOptions options = CompilerOptions.defaultOptions();
    compiler.setOptions(options);

    Node root = NodeFactory.createProgram();
    Node variables = NodeFactory.createBlock(Arrays.asList(
        NodeFactory.createVariable(createIdentifier("a")),
        NodeFactory.createVariable(createIdentifier("b")),
        NodeFactory.createIfStatement(
            NodeFactory.createVariable(createIdentifier("a")),
            NodeFactory.createBlock(Arrays.asList(
                NodeFactory.createVariable(createIdentifier("c"))
            )),
            NodeFactory.createBlock()
        )
    ));
    root.setBody(variables);

    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);

    Node expectedResult = NodeFactory.createBlock(Arrays.asList(
        NodeFactory.createVariable(createIdentifier("a"), NodeFactory.createBlock(
            Arrays.asList(
                NodeFactory.createIfStatement(
                    NodeFactory.createVariable(createIdentifier("a")),
                    NodeFactory.createBlock(Arrays.asList(
                        NodeFactory.createVariable(createIdentifier("c"))
                    )),
                    NodeFactory.createBlock()
                )
            )
        ))
    ));

    assertEquals(expectedResult.toSource(), root.toSource());
  }

  @Test
  public void testCollapsingWithFunctionInConditional() {
    Compiler compiler = new Compiler();
    CompilerOptions options = CompilerOptions.defaultOptions();
    compiler.setOptions(options);

    Node root = NodeFactory.createProgram();
    Node variables = NodeFactory.createBlock(Arrays.asList(
        NodeFactory.createVariable(createIdentifier("a")),
        NodeFactory.createVariable(createIdentifier("b")),
        NodeFactory.createIfStatement(
            NodeFactory.createCall(createIdentifier("test"), Arrays.asList(NodeFactory.createBooleanLiteral(true))),
            NodeFactory.createBlock(Arrays.asList(
                NodeFactory.createVariable(createIdentifier("c"))
            )),
            NodeFactory.createBlock()
        )
    ));
    root.setBody(variables);

    root = NodeFactory.createProgram();
    variables = NodeFactory.createBlock(Arrays.asList(
        NodeFactory.createVariable(createIdentifier("a")),
        NodeFactory.createVariable(createIdentifier("b")),
        NodeFactory.createIfStatement(
            NodeFactory.createCall(createIdentifier("test"), Arrays.asList(NodeFactory.createBooleanLiteral(false))),
            NodeFactory.createBlock(Arrays.asList(
                NodeFactory.createVariable(createIdentifier("c"))
            )),
            NodeFactory.createBlock()
        )
    ));
    root.setBody(variables);

    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);

    Node expectedResult = NodeFactory.createBlock(Arrays.asList(
        NodeFactory.createVariable(createIdentifier("a"), NodeFactory.createBlock(
            Arrays.asList(
                NodeFactory.createIfStatement(
                    NodeFactory.createCall(createIdentifier("test"), Arrays.asList(NodeFactory.createBooleanLiteral(true))),
                    NodeFactory.createBlock(Arrays.asList(
                        NodeFactory.createVariable(createIdentifier("c"))
                    )),
                    NodeFactory.createBlock()
                ),
                NodeFactory.createIfStatement(
                    NodeFactory.createCall(createIdentifier("test"), Arrays.asList(NodeFactory.createBooleanLiteral(false))),
                    NodeFactory.createBlock(Arrays.asList(
                        NodeFactory.createVariable(createIdentifier("c"))
                    )),
                    NodeFactory.createBlock()
                )
            )
        ))
    ));

    assertEquals(expectedResult.toSource(), root.toSource());
  }

  @Test
  public void testCollapsingWithNestedFunction() {
    Compiler compiler = new Compiler();
    CompilerOptions options = CompilerOptions.defaultOptions();
    compiler.setOptions(options);

    Node root = NodeFactory.createProgram();
    Node function1 = NodeFactory.createFunction(
        NodeFactory.createIdentifier("test1"),
        NodeFactory.createBlock(Arrays.asList(
            NodeFactory.createVariable(createIdentifier("a")),
            NodeFactory.createBlock(Arrays.asList(
                NodeFactory.createVariable(createIdentifier("b")),
                NodeFactory.createFunction(
                    NodeFactory.createIdentifier("test2"),
                    NodeFactory.createBlock(Arrays.asList(
                        NodeFactory.createVariable(createIdentifier("c"))
                    ))
                )
            ))
        ))
    );
    root.setBody(function1);

    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);

    Node expectedResult = NodeFactory.createFunction(
        NodeFactory.createIdentifier("test1"),
        NodeFactory.createBlock(Arrays.asList(
            NodeFactory.createVariable(createIdentifier("a"), NodeFactory.createBlock(
                Arrays.asList(
                    NodeFactory.createBlock(Arrays.asList(
                        NodeFactory.createVariable(createIdentifier("b")),
                        NodeFactory.createFunction(
                            NodeFactory.createIdentifier("test2"),
                            NodeFactory.createBlock(Arrays.asList(
                                NodeFactory.createVariable(createIdentifier("c"))
                            ))
                        )
                    ))
                )
            ))
        ))
    );

    assertEquals(expectedResult.toSource(), root.toSource());
  }
}