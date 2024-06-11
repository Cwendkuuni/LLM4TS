package com.google.javascript.jscomp.test;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerTestCase;
import com.google.javascript.jscomp.InlineObjectLiterals;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.jscomp.ReferenceMap;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import java.util.List;
import java.util.Map;

public class InlineObjectLiteralsTest extends CompilerTestCase {

  public InlineObjectLiteralsTest() {
    super();
  }

  @Override
  protected CompilerPass getProcessor(Compiler compiler) {
    return new InlineObjectLiterals(compiler, () -> "test_var_");
  }

  @Override
  protected String getInputFile() {
    return "testInput.js";
  }

  @Override
  protected String getExpectedOutputFile() {
    return "testOutput.js";
  }

  public void testProcess_EmptySourceFile() {
    Node root = NodeOperations.parse("", getSourceLanguage());
    InlineObjectLiterals processor = getProcessor(compiler);
    processor.process(externs, root);
    assertEquals(externs, root);
  }

  public void testProcess_ObjectLiteralAssignment() {
    String input = "var obj = {a: 1, b: 2};";
    Node root = NodeOperations.parse(input, getSourceLanguage());
    InlineObjectLiterals processor = getProcessor(compiler);
    List<Node> expected = ImmutableList.of(
        IR.assign(
            IR.name("a_test_var_"),
            IR.number(1)),
        IR.assign(
            IR.name("b_test_var_"),
            IR.number(2)),
        IR.trueNode()
    );
    processor.process(externs, root);
    assertEquals(expected, getRootChildren(root));
  }

  public void testProcess_ObjectLiteralAssignment_MultipleKeys() {
    String input = "var obj = {a: 1, b: 2, c: 3, d: 4};";
    Node root = NodeOperations.parse(input, getSourceLanguage());
    InlineObjectLiterals processor = getProcessor(compiler);
    List<Node> expected = ImmutableList.of(
        IR.assign(
            IR.name("a_test_var_"),
            IR.number(1)),
        IR.assign(
            IR.name("b_test_var_"),
            IR.number(2)),
        IR.assign(
            IR.name("c_test_var_"),
            IR.number(3)),
        IR.assign(
            IR.name("d_test_var_"),
            IR.number(4)),
        IR.trueNode()
    );
    processor.process(externs, root);
    assertEquals(expected, getRootChildren(root));
  }

  public void testProcess_ObjectLiteralAssignment_EmptyObject() {
    String input = "var obj = {};";
    Node root = NodeOperations.parse(input, getSourceLanguage());
    InlineObjectLiterals processor = getProcessor(compiler);
    List<Node> expected = ImmutableList.of(
        IR.trueNode()
    );
    processor.process(externs, root);
    assertEquals(expected, getRootChildren(root));
  }

  public void testProcess_ObjectLiteralAssignment_ObjectWithGetter() {
    String input = "var obj = {get x() {return 1;}};";
    Node root = NodeOperations.parse(input, getSourceLanguage());
    InlineObjectLiterals processor = getProcessor(compiler);
    List<Node> expected = ImmutableList.of(
        IR.assign(
            IR.name("x_test_var_"),
            IR.number(1)),
        IR.trueNode()
    );
    processor.process(externs, root);
    assertEquals(expected, getRootChildren(root));
  }

  public void testProcess_ObjectLiteralAssignment_SelfReference() {
    String input = "var obj = {get a() {return this.b;}}; obj.b = 1;";
    Node root = NodeOperations.parse(input, getSourceLanguage());
    InlineObjectLiterals processor = getProcessor(compiler);
    List<Node> expected = ImmutableList.of(
        IR.assign(
            IR.name("b_test_var_"),
            IR.number(1)),
        IR.assign(
            IR.name("a_test_var_"),
            IR.getProp(
                IR.name("b_test_var_"),
                IR.string("get")
            )
        ),
        IR.trueNode()
    );
    processor.process(externs, root);
    assertEquals(expected, getRootChildren(root));
  }

  public void testProcess_ObjectLiteralDeclaration() {
    String input = "var obj = {};";
    Node root = NodeOperations.parse(input, getSourceLanguage());
    InlineObjectLiterals processor = getProcessor(compiler);
    List<Node> expected = ImmutableList.of(
        IR.var(IR.name("obj_test_var_"), null),
        IR.trueNode()
    );
    processor.process(externs, root);
    assertEquals(expected, getRootChildren(root));
  }

  public void testProcess_ObjectLiteralDeclaration_MultipleKeys() {
    String input = "var obj = {a: 1, b: 2};";
    Node root = NodeOperations.parse(input, getSourceLanguage());
    InlineObjectLiterals processor = getProcessor(compiler);
    List<Node> expected = ImmutableList.of(
        IR.var(IR.name("obj_test_var_"), null),
        IR.assign(
            IR.name("a_test_var_"),
            IR.number(1)
        ),
        IR.assign(
            IR.name("b_test_var_"),
            IR.number(2)
        ),
        IR.trueNode()
    );
    processor.process(externs, root);
    assertEquals(expected, getRootChildren(root));
  }

  public void testProcess_ObjectLiteralDeclaration_SelfReference() {
    String input = "var obj = {get a() {return this.b;}};";
    Node root = NodeOperations.parse(input, getSourceLanguage());
    InlineObjectLiterals processor = getProcessor(compiler);
    List<Node> expected = ImmutableList.of(
        IR.var(IR.name("obj_test_var_"), null),
        IR.var(IR.name("b_test_var_"), null),
        IR.getProp(
            IR.name("obj_test_var_"),
            IR.string("get")
        ),
        IR.assign(
            IR.name("a_test_var_"),
            IR.getProp(
                IR.name("b_test_var_"),
                IR.string("get")
            )
        ),
        IR.trueNode()
    );
    processor.process(externs, root);
    assertEquals(expected, getRootChildren(root));
  }

  public void testProcess_ObjectLiteralDeclarationWithInitialValue() {
    String input = "var obj = {a: 1, b: 2};";
    Node root = NodeOperations.parse(input, getSourceLanguage());
    InlineObjectLiterals processor = getProcessor(compiler);
    List<Node> expected = ImmutableList.of(
        IR.var(IR.name("obj_test_var_"), null),
        IR.assign(
            IR.name("a_test_var_"),
            IR.number(1)
        ),
        IR.assign(
            IR.name("b_test_var_"),
            IR.number(2)
        ),
        IR.trueNode()
    );

    Map<String, Node> initvals = Maps.newHashMap();
    ReferenceCollection refCollection = ReferenceCollectingCallback.collect(
        new ReferenceCollectingCallback.Reference(null, root, null), compiler);
    processor.splitObject(
        new Scope.Var(IR.name("obj_test_var_"), null, null),
        new Reference(null, root, null),
        new Reference(null, root, null),
        refCollection
    );
    List<Node> actual = Lists.newArrayList(getRootChildren(root));
    assertEquals(expected, actual);
  }

  public void testProcess_ObjectLiteralDeclarationWithInitialValue_MultipleKeys() {
    String input = "var obj = {a: 1, b: 2, c: 3};";
    Node root = NodeOperations.parse(input, getSourceLanguage());
    InlineObjectLiterals processor = getProcessor(compiler);
    List<Node> expected = ImmutableList.of(
        IR.var(IR.name("obj_test_var_"), null),
        IR.assign(
            IR.name("a_test_var_"),
            IR.number(1)
        ),
        IR.assign(
            IR.name("b_test_var_"),
            IR.number(2)
        ),
        IR.assign(
            IR.name("c_test_var_"),
            IR.number(3)
        ),
        IR.trueNode()
    );

    Map<String, Node> initvals = Maps.newHashMap();
    ReferenceCollection refCollection = ReferenceCollectingCallback.collect(
        new ReferenceCollectingCallback.Reference(null, root, null), compiler);
    processor.splitObject(
        new Scope.Var(IR.name("obj_test_var_"), null, null),
        new Reference(null, root, null),
        new Reference(null, root, null),
        refCollection
    );
    List<Node> actual = Lists.newArrayList(getRootChildren(root));
    assertEquals(expected, actual);
  }

  public void testProcess_ObjectLiteralDeclarationWithInitialValue_SelfReference() {
    String input = "var obj = {get a() {return this.b;}};";
    Node root = NodeOperations.parse(input, getSourceLanguage());
    InlineObjectLiterals processor = getProcessor(compiler);
    List<Node> expected = ImmutableList.of(
        IR.var(IR.name("obj_test_var_"), null),
        IR.var(IR.name("b_test_var_"), null),
        IR.getProp(
            IR.name("obj_test_var_"),
            IR.string("get")
        ),
        IR.assign(
            IR.name("a_test_var_"),
            IR.getProp(
                IR.name("b_test_var_"),
                IR.string("get")
            )
        ),
        IR.trueNode()
    );

    Map<String, Node> initvals = Maps.newHashMap();
    ReferenceCollection refCollection = ReferenceCollectingCallback.collect(
        new ReferenceCollectingCallback.Reference(null, root, null), compiler);
    processor.splitObject(
        new Scope.Var(IR.name("obj_test_var_"), null, null),
        new Reference(null, root, null),
        new Reference(null, root, null),
        refCollection
    );
    List<Node> actual = Lists.newArrayList(getRootChildren(root));
    assertEquals(expected, actual);
  }
}