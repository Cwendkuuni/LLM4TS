import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.JSDocInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CollapseVariableDeclarationsTest {

 private AbstractCompiler compiler;
 private Node root;
 private Scope scope;

 @org.junit.Before
 public void setUp() {
 compiler = mock(AbstractCompiler.class);
 scope = mock(Scope.class);
 root = new Node(Token.PROGRAM);
 }

 @org.junit.Test
 public void testProcess_NoCollapses() {
 CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
 pass.process(null, root);
 assertFalse(pass.collapses.isEmpty());
 }

 @org.junit.Test
 public void testProcess_SingleDeclaration() {
 Node declaration = new Node(Token.VAR, "a");
 root.addChildToBack(declaration);

 CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
 pass.process(null, root);
 assertFalse(pass.collapses.isEmpty());

 Collapse collapse = pass.collapses.get(0);
 assertNotNull(collapse.startNode);
 assertNotNull(collapse.endNode);
 assertNull(collapse.parent);
 }

 @org.junit.Test
 public void testProcess_MultipleDeclarations() {
 Node firstDeclaration = new Node(Token.VAR, "a");
 Node secondDeclaration = new Node(Token.VAR, "b");
 root.addChildToBack(firstDeclaration);
 root.addChildToBack(secondDeclaration);

 CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
 pass.process(null, root);
 assertFalse(pass.collapses.isEmpty());

 Collapse collapse = pass.collapses.get(0);
 assertNotNull(collapse.startNode);
 assertNotNull(collapse.endNode);
 assertNotNull(collapse.parent);
 }

 @org.junit.Test
 public void testProcess_Assignments() {
 Node declaration = new Node(Token.VAR, "a");
 Node assignment = new Node(Token.ASSIGN, null, new Node(Token.NUM, 1));
 declaration.addChildToBack(assignment);
 Node nextDeclaration = new Node(Token.VAR, "b");
 root.addChildToBack(declaration);
 root.addChildToBack(nextDeclaration);

 CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
 pass.process(null, root);
 assertFalse(pass.collapses.isEmpty());

 Collapse collapse = pass.collapses.get(0);
 assertNotNull(collapse.startNode);
 assertNotNull(collapse.endNode);
 assertNotNull(collapse.parent);
 }

 @org.junit.Test
 public void testProcess_CollapsesMultipleAssignments() {
 Node firstDeclaration = new Node(Token.VAR, "a");
 Node firstAssignment = new Node(Token.ASSIGN, null, new Node(Token.NUM, 1));
 firstDeclaration.addChildToBack(firstAssignment);
 Node secondDeclaration = new Node(Token.VAR, "b");
 Node secondAssignment = new Node(Token.ASSIGN, null, new Node(Token.NUM, 1));
 root.addChildToBack(firstDeclaration);
 root.addChildToBack(secondDeclaration);

 CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
 pass.process(null, root);
 assertFalse(pass.collapses.isEmpty());

 Collapse collapse = pass.collapses.get(0);
 assertNotNull(collapse.startNode);
 assertNotNull(collapse.endNode);
 assertNotNull(collapse.parent);

 Node collapsesVar = collapse.startNode;
 assertNull(collapsesVar.getNext());

 Node collapsesAssignment = collapsesVar.getFirstChild();
 assertNotNull(collapsesAssignment.getNext());
 Node nextAssignment = collapsesAssignment.getNext();
 assertNull(nextAssignment.getNext());

 assertNotNull(nextAssignment.getChild(0));
 assertNull(collapsesAssignment.getChild(1));
 }

 @org.junit.Test
 public void testProcess_CollapsesAssignmentsIntoDeclaration() {
 Node declaration = new Node(Token.VAR, "a");
 Node assignment = new Node(Token.ASSIGN, null, new Node(Token.NUM, 1));
 Node nextDeclaration = new Node(Token.VAR, "b");
 root.addChildToBack(declaration);
 root.addChildToBack(assignment);
 root.addChildToBack(nextDeclaration);

 CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
 pass.process(null, root);
 assertFalse(pass.collapses.isEmpty());

 Collapse collapse = pass.collapses.get(0);
 assertNotNull(collapse.startNode);
 assertNotNull(collapse.endNode);
 assertNotNull(collapse.parent);

 Node collapsesVar = collapse.startNode;
 assertNull(collapsesVar.getNext());

 Node collapsesAssignment = collapsesVar.getFirstChild();
 assertNull(collapsesAssignment.getNext());
 assertNotNull(collapsesAssignment.getChild(0));
 }

 @org.junit.Test
 public void testProcess_CollapsesVariableDeclarationsIntoSingleDeclaration() {
 Node firstDeclaration = new Node(Token.VAR, "a");
 Node secondDeclaration = new Node(Token.VAR, "b");
 root.addChildToBack(firstDeclaration);
 root.addChildToBack(secondDeclaration);

 CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
 pass.process(null, root);
 assertFalse(pass.collapses.isEmpty());

 Collapse collapse = pass.collapses.get(0);
 assertNotNull(collapse.startNode);
 assertNotNull(collapse.endNode);
 assertNull(collapse.parent);

 Node collapsesVar = collapse.startNode;
 Node collapsesNext = collapsesVar.getNext();
 assertNotNull(collapsesNext);

 assertNotNull(collapsesVar.getChild(0));
 assertNull(collapsesNext.getChild(0));
 }

 @org.junit.Test
 public void testProcess_CollapsesVariableDeclarationsIntoSingleDeclarationWithAssignments() {
 Node firstDeclaration = new Node(Token.VAR, "a");
 Node firstAssignment = new Node(Token.ASSIGN, null, new Node(Token.NUM, 1));
 firstDeclaration.addChildToBack(firstAssignment);
 Node secondDeclaration = new Node(Token.VAR, "b");
 Node secondAssignment = new Node(Token.ASSIGN, null, new Node(Token.NUM, 1));
 root.addChildToBack(firstDeclaration);
 root.addChildToBack(secondDeclaration);
 root.addChildToBack(firstAssignment);
 root.addChildToBack(secondAssignment);

 CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
 pass.process(null, root);
 assertFalse(pass.collapses.isEmpty());

 Collapse collapse = pass.collapses.get(0);
 assertNotNull(collapse.startNode);
 assertNotNull(collapse.endNode);
 assertNull(collapse.parent);

 Node collapsesVar = collapse.startNode;
 Node collapsesNext = collapsesVar.getNext();
 assertNotNull(collapsesNext);

 assertNotNull(collapsesVar.getChild(0));
 assertNull(collapsesNext.getChild(0));

 Node collapsesAssignment = collapsesVar.getFirstChild();
 assertNull(collapsesAssignment.getNext());
 assertNotNull(collapsesAssignment.getChild(0));
 }

 @org.junit.Test
 public void testProcess_CollapsesVariableDeclarationsWithDifferentNames() {
 Node firstDeclaration = new Node(Token.VAR, "a");
 Node secondDeclaration = new Node(Token.VAR, "b");
 root.addChildToBack(firstDeclaration);
 root.addChildToBack(secondDeclaration);

 CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
 pass.process(null, root);
 assertFalse(pass.collapses.isEmpty());

 Collapse collapse = pass.collapses.get(0);
 assertNotNull(collapse.startNode);
 assertNotNull(collapse.endNode);
 assertNull(collapse.parent);
 }

 @org.junit.Test
 public void testProcess_CollapsesDeclarationsWithSameName() {
 Node firstDeclaration = new Node(Token.VAR, "a");
 Node secondDeclaration = new Node(Token.VAR, "a");
 root.addChildToBack(firstDeclaration);
 root.addChildToBack(secondDeclaration);

 CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
 pass.process(null, root);
 assertFalse(pass.collapses.isEmpty());

 Collapse collapse = pass.collapses.get(0);
 assertNotNull(collapse.startNode);
 assertNotNull(collapse.endNode);
 assertNull(collapse.parent);

 Node collapsesVar = collapse.startNode;
 Node collapsesNext = collapsesVar.getNext();
 assertNotNull(collapsesNext);

 assertNotNull(collapsesVar.getChild(0));
 assertNull(collapsesNext.getChild(0));
 }

 @org.junit.Test
 public void testProcess_CollapsesVariableDeclarationsWithJSDocInfo() {
 Node firstDeclaration = new Node(Token.VAR, "a");
 firstDeclaration.setJSDocInfo(new JSDocInfo());
 Node secondDeclaration = new Node(Token.VAR, "b");
 root.addChildToBack(firstDeclaration);
 root.addChildToBack(secondDeclaration);

 CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
 pass.process(null, root);
 assertFalse(pass.collapses.isEmpty());

 Collapse collapse = pass.collapses.get(0);
 assertNotNull(collapse.startNode);
 assertNotNull(collapse.endNode);
 assertNull(collapse.parent);

 Node collapsesVar = collapse.startNode;
 Node collapsesNext = collapsesVar.getNext();
 assertNotNull(collapsesNext);

 assertNotNull(collapsesVar.getChild(0));
 assertNull(collapsesNext.getChild(0));

 Node collapsesVarJSDoc = collapsesVar.getFirstChild();
 Node collapsesNextJSDoc = collapsesNext.getFirstChild();
 assertNotNull(collapsesVarJSDoc.getJSDocInfo());
 assertNull(collapsesNextJSDoc.getJSDocInfo());
 }

 @org.junit.Test
 public void testProcess_CollapsesVariableDeclarationsWithJSDocInfoAndAssignments() {
 Node firstDeclaration = new Node(Token.VAR, "a");
 firstDeclaration.setJSDocInfo(new JSDocInfo());
 Node firstAssignment = new Node(Token.ASSIGN, null, new Node(Token.NUM, 1));
 firstDeclaration.addChildToBack(firstAssignment);
 Node secondDeclaration = new Node(Token.VAR, "b");
 Node secondAssignment = new Node(Token.ASSIGN, null, new Node(Token.NUM, 1));
 root.addChildToBack(firstDeclaration);
 root.addChildToBack(secondDeclaration);
 root.addChildToBack(firstAssignment);
 root.addChildToBack(secondAssignment);

 CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
 pass.process(null, root);
 assertFalse(pass.collapses.isEmpty());

 Collapse collapse = pass.collapses.get(0);
 assertNotNull(collapse.startNode);
 assertNotNull(collapse.endNode);
 assertNull(collapse.parent);

 Node collapsesVar = collapse.startNode;
 Node collapsesNext = collapsesVar.getNext();
 assertNotNull(collapsesNext);

 assertNotNull(collapsesVar.getChild(0));
 assertNull(collapsesNext.getChild(0));

 Node collapsesVarJSDoc = collapsesVar.getFirstChild();
 Node collapsesNextJSDoc = collapsesNext.getFirstChild();
 assertNotNull(collapsesVarJSDoc.getJSDocInfo());
 assertNull(collapsesNextJSDoc.getJSDocInfo());
 }

 @org.junit.Test
 public void testProcess_CollapsesVariableDeclarationsWithJSDocInfoAndAssignmentsAndJSDocSuppression() {
 Node firstDeclaration = new Node(Token.VAR, "a");
 firstDeclaration.setJSDocInfo(new JSDocInfo());
 firstDeclaration.getJSDocInfo().addSuppression("duplicate");
 Node firstAssignment = new Node(Token.ASSIGN, null, new Node(Token.NUM, 1));
 firstDeclaration.addChildToBack(firstAssignment);
 Node secondDeclaration = new Node(Token.VAR, "b");
 Node secondAssignment = new Node(Token.ASSIGN, null, new Node(Token.NUM, 1));
 root.addChildToBack(firstDeclaration);
 root.addChildToBack(secondDeclaration);
 root.addChildToBack(firstAssignment);
 root.addChildToBack(secondAssignment);

 CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
 pass.process(null, root);
 assertFalse(pass.collapses.isEmpty());

 Collapse collapse = pass.collapses.get(0);
 assertNotNull(collapse.startNode);
 assertNotNull(collapse.endNode);
 assertNull(collapse.parent);

 Node collapsesVar = collapse.startNode;
 Node collapsesNext = collapsesVar.getNext();
 assertNotNull(collapsesNext);

 assertNotNull(collapsesVar.getChild(0));
 assertNull(collapsesNext.getChild(0));

 Node collapsesVarJSDoc = collapsesVar.getFirstChild();
 Node collapsesNextJSDoc = collapsesNext.getFirstChild();
 assertNotNull(collapsesVarJSDoc.getJSDocInfo());
 assertNotNull(collapsesNextJSDoc.getJSDocInfo());
 assertFalse(collapsesVarJSDoc.getJSDocInfo().getSuppressed().isEmpty());
 assertFalse(collapsesNextJSDoc.getJSDocInfo().getSuppressed().isEmpty());
 }

 @org.junit.Test
 public void testProcess_CollapsesVariableDeclarationsWithJSDocInfoAndAssignmentsAndJSDocSuppression_EmptySuppressionList() {
 Node firstDeclaration = new Node(Token.VAR, "a");
 firstDeclaration.setJSDocInfo(new JSDocInfo());
 Node firstAssignment = new Node(Token.ASSIGN, null, new Node(Token.NUM, 1));
 firstDeclaration.addChildToBack(firstAssignment);
 Node secondDeclaration = new Node(Token.VAR, "b");
 Node secondAssignment = new Node(Token.ASSIGN, null, new Node(Token.NUM, 1));
 root.addChildToBack(firstDeclaration);
 root.addChildToBack(secondDeclaration);
 root.addChildToBack(firstAssignment);
 root.addChildToBack(secondAssignment);

 CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
 pass.process(null, root);
 assertFalse(pass.collapses.isEmpty());

 Collapse collapse = pass.collapses.get(0);
 assertNotNull(collapse.startNode);
 assertNotNull(collapse.endNode);
 assertNull(collapse.parent);

 Node collapsesVar = collapse.startNode;
 Node collapsesNext = collapsesVar.getNext();
 assertNotNull(collapsesNext);

 assertNotNull(collapsesVar.getChild(0));
 assertNull(collapsesNext.getChild(0));

 Node collapsesVarJSDoc = collapsesVar.getFirstChild();
 Node collapsesNextJSDoc = collapsesNext.getFirstChild();
 assertNotNull(collapsesVarJSDoc.getJSDocInfo());
 assertNotNull(collapsesNextJSDoc.getJSDocInfo());
 assertFalse(collapsesVarJSDoc.getJSDocInfo().getSuppressed().isEmpty());
 assertTrue(collapsesVarJSDoc.getJSDocInfo().getSuppressed().isEmpty());
 assertFalse(collapsesNextJSDoc.getJSDocInfo().getSuppressed().isEmpty());
 assertTrue(collapsesNextJSDoc.getJSDocInfo().getSuppressed().isEmpty());
 }
}