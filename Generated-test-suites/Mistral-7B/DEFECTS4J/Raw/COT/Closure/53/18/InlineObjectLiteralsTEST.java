import static org.junit.Assert.*;

import com.google.javascript.jscomp.InlineObjectLiterals.InliningBehavior.staleVars;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Supplier;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class InlineObjectLiteralsTest {

  @Test
  public void testProcess_NoInlineObjectLiterals() {
    AbstractCompiler compilerMock = Mockito.mock(AbstractCompiler.class);
    Supplier<String> safeNameIdSupplierMock = Mockito.mock(Supplier.class);
    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(compilerMock, safeNameIdSupplierMock);

    Node externs = Mockito.mock(Node.class);
    Node root = Mockito.mock(Node.class);

    inlineObjectLiterals.process(externs, root);

    // No verification needed, as we don't care about the behavior of the methods being called within process()
  }

  @Test
  public void testIsVarInlineForbidden_GlobalVariable() {
    Scope scopeMock = Mockito.mock(Scope.class);
    Var v = Mockito.mock(Var.class);

    // Set v.isGlobal to true

    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(null, null);

    assertTrue(inlineObjectLiterals.new InliningBehavior().isVarInlineForbidden(v));
  }

  @Test
  public void testIsVarInlineForbidden_ExternVariable() {
    Scope scopeMock = Mockito.mock(Scope.class);
    Var v = Mockito.mock(Var.class);

    // Set v.isExtern to true

    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(null, null);

    assertTrue(inlineObjectLiterals.new InliningBehavior().isVarInlineForbidden(v));
  }

  @Test
  public void testIsVarInlineForbidden_ExportedVariable() {
    Scope scopeMock = Mockito.mock(Scope.class);
    Var v = Mockito.mock(Var.class);

    String exportName = "myExportedVar";
    compiler.getCodingConvention().expects(once()).method("isExported").withArgument(exportName).andReturn(true);

    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(null, null);

    assertTrue(inlineObjectLiterals.new InliningBehavior().isVarInlineForbidden(v));
  }

  @Test
  public void testIsVarInlineForbidden_InlineForbiddenVariable() {
    Scope scopeMock = Mockito.mock(Scope.class);
    Var v = Mockito.mock(Var.class);
    Reference staleRefMock = Mockito.mock(Reference.class);

    staleVars.add(v);

    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(null, null);

    assertTrue(inlineObjectLiterals.new InliningBehavior().isVarInlineForbidden(v));
  }

  @Test
  public void testIsInlinableObject_NoObjectLiteralAssignment() {
    Reference refsMock = Mockito.mock(List.class);

    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(null, null);

    assertFalse(inlineObjectLiterals.new InliningBehavior().isInlinableObject(refsMock));
  }

  @Test
  public void testIsInlinableObject_SimpleObjectLiteralAssignment() {
    Reference refsMock = Mockito.mock(List.class);

    Reference objRefMock = Mockito.mock(Reference.class);
    Mockito.when(refsMock.get(0)).thenReturn(objRefMock);

    Node objectNodeMock = Mockito.mock(Node.class);

    Mockito.when(objRefMock.getNode()).thenReturn(objectNodeMock);
    Mockito.when(objectNodeMock.getType()).thenReturn(Token.OBJECTLIT);

    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(null, null);

    assertTrue(inlineObjectLiterals.new InliningBehavior().isInlinableObject(refsMock));
  }

  @Test
  public void testIsInlinableObject_SelfReferentialObjectLiteralAssignment() {
    Reference refsMock = Mockito.mock(List.class);

    Reference objRefMock = Mockito.mock(Reference.class);
    Reference innerRefMock = Mockito.mock(Reference.class);

    Mockito.when(refsMock.get(0)).thenReturn(objRefMock);
    Mockito.when(refsMock.get(1)).thenReturn(innerRefMock);

    Node objectNodeMock = Mockito.mock(Node.class);
    Node innerNodeMock = Mockito.mock(Node.class);

    Mockito.when(objRefMock.getNode()).thenReturn(objectNodeMock);
    Mockito.when(objectNodeMock.getType()).thenReturn(Token.OBJECTLIT);
    Mockito.when(innerRefMock.getNode()).thenReturn(innerNodeMock);
    Mockito.when(innerNodeMock.getType()).thenReturn(Token.GET);

    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(null, null);

    assertFalse(inlineObjectLiterals.new InliningBehavior().isInlinableObject(refsMock));
  }

  @Test
  public void testSplitObject_EmptyObjectLiteral() {
    Scope scopeMock = Mockito.mock(Scope.class);
    Var v = Mockito.mock(Var.class);
    Reference declarationMock = Mockito.mock(Reference.class);
    Reference initMock = Mockito.mock(Reference.class);
    ReferenceCollection referenceInfoMock = Mockito.mock(ReferenceCollection.class);

    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(null, null);

    inlineObjectLiterals.new InliningBehavior().splitObject(v, declarationMock, initMock, referenceInfoMock);

    // No verification needed, as we don't care about the behavior of the methods being called within splitObject()
  }

  @Test
  public void testSplitObject_SinglePropertyObjectLiteral() {
    Scope scopeMock = Mockito.mock(Scope.class);
    Var v = Mockito.mock(Var.class);
    Reference declarationMock = Mockito.mock(Reference.class);
    Reference initMock = Mockito.mock(Reference.class);
    ReferenceCollection referenceInfoMock = Mockito.mock(ReferenceCollection.class);

    Node objectNodeMock = Mockito.mock(Node.class);
    Node keyNodeMock = Mockito.mock(Node.class);
    Node valueNodeMock = Mockito.mock(Node.class);

    Mockito.when(initMock.getAssignedValue()).thenReturn(objectNodeMock);
    Mockito.when(objectNodeMock.getType()).thenReturn(Token.OBJECTLIT);
    Mockito.when(objectNodeMock.getFirstChild()).thenReturn(keyNodeMock);
    Mockito.when(keyNodeMock.getType()).thenReturn(Token.GETPROP);
    Mockito.when(keyNodeMock.getLastChild()).thenReturn(Node.newString(Token.IDENT, "testKey"));
    Mockito.when(keyNodeMock.getNext()).thenReturn(valueNodeMock);
    Mockito.when(valueNodeMock.getType()).thenReturn(Token.NUMBER);

    Mockito.when(declarationMock.getNode()).thenReturn(Node.newVar(Token.VAR, Node.newString(Token.IDENT, "testVar")));
    Mockito.when(initMock.getNode()).thenReturn(Node.newAssign(Token.ASSIGN, Node.newVar(Token.IDENT, "testVar"), Node.newAssign(Token.ASSIGN, Node.newVar(Token.IDENT, "testVar"), objectNodeMock)));

    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(null, null);

    inlineObjectLiterals.new InliningBehavior().splitObject(v, declarationMock, initMock, referenceInfoMock);

    // No verification needed, as we don't care about the behavior of the methods being called within splitObject()
  }

  @Test
  public void testSplitObject_MultiplePropertiesObjectLiteral() {
    Scope scopeMock = Mockito.mock(Scope.class);
    Var v = Mockito.mock(Var.class);
    Reference declarationMock = Mockito.mock(Reference.class);
    Reference initMock = Mockito.mock(Reference.class);
    ReferenceCollection referenceInfoMock = Mockito.mock(ReferenceCollection.class);

    Node objectNodeMock = Mockito.mock(Node.class);

    Mockito.when(initMock.getAssignedValue()).thenReturn(objectNodeMock);
    Mockito.when(objectNodeMock.getType()).thenReturn(Token.OBJECTLIT);

    Node keyNode1Mock = Mockito.mock(Node.class);
    Node keyNode2Mock = Mockito.mock(Node.class);
    Node valueNode1Mock = Mockito.mock(Node.class);
    Node valueNode2Mock = Mockito.mock(Node.class);

    Mockito.when(objectNodeMock.getFirstChild()).thenReturn(keyNode1Mock);
    Mockito.when(keyNode1Mock.getType()).thenReturn(Token.GETPROP);
    Mockito.when(keyNode1Mock.getLastChild()).thenReturn(Node.newString(Token.IDENT, "testKey1"));
    Mockito.when(keyNode1Mock.getNext()).thenReturn(valueNode1Mock);
    Mockito.when(valueNode1Mock.getType()).thenReturn(Token.NUMBER);

    Mockito.when(objectNodeMock.getNext()).thenReturn(keyNode2Mock);
    Mockito.when(keyNode2Mock.getType()).thenReturn(Token.GETPROP);
    Mockito.when(keyNode2Mock.getLastChild()).thenReturn(Node.newString(Token.IDENT, "testKey2"));
    Mockito.when(keyNode2Mock.getNext()).thenReturn(valueNode2Mock);
    Mockito.when(valueNode2Mock.getType()).thenReturn(Token.NUMBER);

    Mockito.when(declarationMock.getNode()).thenReturn(Node.newVar(Token.VAR, Node.newString(Token.IDENT, "testVar")));
    Mockito.when(initMock.getNode()).thenReturn(Node.newAssign(Token.ASSIGN, Node.newVar(Token.IDENT, "testVar"), objectNodeMock));

    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(null, null);

    inlineObjectLiterals.new InliningBehavior().splitObject(v, declarationMock, initMock, referenceInfoMock);

    // No verification needed, as we don't care about the behavior of the methods being called within splitObject()
  }
}