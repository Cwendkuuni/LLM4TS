package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Behavior;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Reference;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceCollection;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceMap;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import org.junit.Test;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    Node root = new Node(Token.PROGRAM, null);
    InlineObjectLiterals pass = new InlineObjectLiterals(new TestCompiler(), () -> "");

    ReferenceCollectingCallback callback = new ReferenceCollectingCallback(pass.compiler, pass.new InliningBehavior());
    callback.process(null, root);

    assertNotNull(root);

    // Test an object literal with no references
    Node objLiteral1 = new Node(Token.OBJECT_LITERAL, null);
    Node prop1 = new Node(Token.PROPERTY, null, new Node(Token.STRING_LITERAL, "prop1", null), null);
    objLiteral1.addChildToFront(prop1);
    prop1.addChildToFront(new Node(Token.STRING_LITERAL, "value1", null));

    root.addChildToFront(objLiteral1);

    pass.process(null, root);

    Node expectedObjLiteral1 = new Node(Token.OBJECT_LITERAL, null);
    Node varProp1 = new Node(Token.VAR, null, new Node(Token.NAME, "prop1_0"), null);
    Node varValue1 = new Node(Token.VAR, null, new Node(Token.NAME, "value1_0"), null);
    Node assignProp1 = new Node(Token.ASSIGN, null, varProp1, new Node(Token.NAME, "prop1_0"));
    Node assignValue1 = new Node(Token.ASSIGN, null, varValue1, new Node(Token.NAME, "value1_0"));

    expectedObjLiteral1.addChildToFront(assignProp1);
    expectedObjLiteral1.addChildToFront(assignValue1);

    Node actualObjLiteral1 = root.getFirstChild();

    assertEquals(expectedObjLiteral1, actualObjLiteral1);

    // Test an object literal with references
    Node objLiteral2 = new Node(Token.OBJECT_LITERAL, null);
    Node prop2 = new Node(Token.PROPERTY, null, new Node(Token.STRING_LITERAL, "prop2", null), null);
    objLiteral2.addChildToFront(prop2);
    prop2.addChildToFront(new Node(Token.STRING_LITERAL, "value2", null));

    Node refProp2 = new Node(Token.NAME, "prop2", null);
    Node assignProp2 = new Node(Token.ASSIGN, null, refProp2, new Node(Token.NAME, "prop2"));

    Node refObjLiteral2 = new Node(Token.NAME, "objLiteral2", null);
    Node getProp2 = new Node(Token.GETPROP, null, refObjLiteral2, new Node(Token.STRING_LITERAL, "prop2", null));

    root.addChildToFront(objLiteral2);
    root.addChildToFront(assignProp2);
    root.addChildToFront(refObjLiteral2);

    pass.process(null, root);

    Node expectedObjLiteral2 = new Node(Token.OBJECT_LITERAL, null);
    Node varProp2 = new Node(Token.VAR, null, new Node(Token.NAME, "prop2_1"), null);
    Node varValue2 = new Node(Token.VAR, null, new Node(Token.NAME, "value2_1"), null);
    Node assignProp2_expected = new Node(Token.ASSIGN, null, varProp2, new Node(Token.NAME, "prop2_1"));
    Node assignValue2_expected = new Node(Token.ASSIGN, null, varValue2, new Node(Token.NAME, "value2_1"));

    expectedObjLiteral2.addChildToFront(assignProp2_expected);
    expectedObjLiteral2.addChildToFront(assignValue2_expected);

    Node actualObjLiteral2 = root.getFirstChild(1);

    assertEquals(expectedObjLiteral2, actualObjLiteral2);

    // Test an object literal with multiple references
    Node objLiteral3 = new Node(Token.OBJECT_LITERAL, null);
    Node prop3 = new Node(Token.PROPERTY, null, new Node(Token.STRING_LITERAL, "prop3", null), null);
    objLiteral3.addChildToFront(prop3);
    prop3.addChildToFront(new Node(Token.STRING_LITERAL, "value3", null));

    Node refProp3 = new Node(Token.NAME, "prop3", null);
    Node assignProp3 = new Node(Token.ASSIGN, null, refProp3, new Node(Token.NAME, "prop3"));

    Node refObjLiteral3_1 = new Node(Token.NAME, "objLiteral3_1", null);
    Node getProp3_1 = new Node(Token.GETPROP, null, refObjLiteral3_1, new Node(Token.STRING_LITERAL, "prop3", null));

    Node refObjLiteral3_2 = new Node(Token.NAME, "objLiteral3_2", null);
    Node getProp3_2 = new Node(Token.GETPROP, null, refObjLiteral3_2, new Node(Token.STRING_LITERAL, "prop3", null));

    root.addChildToFront(objLiteral3);
    root.addChildToFront(assignProp3);
    root.addChildToFront(refObjLiteral3_1);
    root.addChildToFront(getProp3_1);
    root.addChildToFront(refObjLiteral3_2);
    root.addChildToFront(getProp3_2);

    pass.process(null, root);

    Node expectedObjLiteral3 = new Node(Token.OBJECT_LITERAL, null);
    Node varProp3_1 = new Node(Token.VAR, null, new Node(Token.NAME, "prop3_2"), null);
    Node varValue3_1 = new Node(Token.VAR, null, new Node(Token.NAME, "value3_2"), null);
    Node assignProp3_1 = new Node(Token.ASSIGN, null, varProp3_1, new Node(Token.NAME, "prop3_2"));
    Node assignValue3_1 = new Node(Token.ASSIGN, null, varValue3_1, new Node(Token.NAME, "value3_2"));
    Node varProp3_2 = new Node(Token.VAR, null, new Node(Token.NAME, "prop3_3"), null);
    Node varValue3_2 = new Node(Token.VAR, null, new Node(Token.NAME, "value3_3"), null);
    Node assignProp3_2 = new Node(Token.ASSIGN, null, varProp3_2, new Node(Token.NAME, "prop3_3"));
    Node assignValue3_2 = new Node(Token.ASSIGN, null, varValue3_2, new Node(Token.NAME, "value3_3"));

    expectedObjLiteral3.addChildToFront(assignProp3_1);
    expectedObjLiteral3.addChildToFront(assignValue3_1);
    expectedObjLiteral3.addChildToFront(assignProp3_2);
    expectedObjLiteral3.addChildToFront(assignValue3_2);

    Node actualObjLiteral3 = root.getFirstChild(1);

    assertEquals(expectedObjLiteral3, actualObjLiteral3);
  }

  @Test
  public void testInlineObjectLiteralsWithSelfReference() {
    Node root = new Node(Token.PROGRAM, null);

    InlineObjectLiterals pass = new InlineObjectLiterals(new TestCompiler(), () -> "");

    ReferenceCollectingCallback callback = new ReferenceCollectingCallback(pass.compiler, pass.new InliningBehavior());
    callback.process(null, root);

    // Test an object literal with a self-reference
    Node objLiteralSelfRef = new Node(Token.OBJECT_LITERAL, null);
    Node propSelfRef = new Node(Token.PROPERTY, null, new Node(Token.STRING_LITERAL, "propSelfRef", null), null);
    objLiteralSelfRef.addChildToFront(propSelfRef);
    propSelfRef.addChildToFront(new Node(Token.STRING_LITERAL, "valueSelfRef", null));

    Node refPropSelfRef = new Node(Token.NAME, "propSelfRef", null);
    Node assignPropSelfRef = new Node(Token.ASSIGN, null, refPropSelfRef, new Node(Token.NAME, "propSelfRef"));

    Node refObjLiteralSelfRef = new Node(Token.NAME, "objLiteralSelfRef", null);
    Node getPropSelfRef = new Node(Token.GETPROP, null, refObjLiteralSelfRef, new Node(Token.STRING_LITERAL, "propSelfRef", null));

    root.addChildToFront(objLiteralSelfRef);
    root.addChildToFront(assignPropSelfRef);
    root.addChildToFront(refObjLiteralSelfRef);

    pass.process(null, root);

    // The test will fail as this case is not handled by the pass
    Node expectedObjLiteralSelfRef = objLiteralSelfRef;
    Node actualObjLiteralSelfRef = root.getFirstChild(1);

    assertNull(expectedObjLiteralSelfRef);
    assertNull(actualObjLiteralSelfRef);
  }
}

class TestCompiler extends CompilerPassSetup {
  @Override
  public Compiler pass(Compiler compiler) {
    return new InlineObjectLiterals(compiler, () -> "");
  }
}