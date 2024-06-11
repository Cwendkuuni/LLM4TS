import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeTraversal.ScopedCallback;
import com.google.javascript.jscomp.RenameLabels;
import com.google.javascript.jscomp.RenameLabels.DefaultNameSupplier.LabelInfo;
import com.google.javascript.jscomp.RenameLabels.DefaultNameSupplier.LabelNamespace;
import com.google.javascript.rhino.Token;

@PowerMockIgnore({"javax.management.*", "javax.script.*"})
@RunWith(PowerMockRunner.class)
public class RenameLabelsTest {

 private Compiler mockCompiler;
 private Node rootNode;

 @Before
 public void setUp() {
 mockCompiler = Mockito.mock(Compiler.class);
 rootNode = Mockito.mock(Node.class);
 PowerMockito.when(mockCompiler.getSourceFile()).thenReturn(new Object() {});
 }

 @Test
 public void testProcessLabels() {
 RenameLabels renameLabels = new RenameLabels(mockCompiler);

 NodeTraversal nodeTraversal = Mockito.mock(NodeTraversal.class);
 PowerMockito.when(nodeTraversal.getParent()).thenReturn(rootNode);
 PowerMockito.when(nodeTraversal.traverseTopDown(Mockito.any(Node.class),
 Mockito.any(ScopedCallback.class))).thenReturn(null);

 ScopedCallback scopedCallback = new RenameLabels.ProcessLabels();
 renameLabels.process(null, rootNode);

 PowerMockito.verifyStatic(NodeTraversal.class).traverse(mockCompiler, rootNode,
 scopedCallback);
 }

 @Test
 public void testVisitLabelWithNewName() {
 RenameLabels renameLabels = new RenameLabels(mockCompiler);

 Node labelNode = Mockito.mock(Node.class);
 PowerMockito.when(labelNode.getFirstChild()).thenReturn(Mockito.mock(Node.class));
 PowerMockito.when(labelNode.getLastChild()).thenReturn(Mockito.mock(Node.class));
 PowerMockito.when(labelNode.getType()).thenReturn(Token.LABEL);
 PowerMockito.when(labelNode.getParent()).thenReturn(rootNode);
 PowerMockito.when(mockCompiler.reportCodeChange()).thenReturn(null);

 NodeTraversal nodeTraversal = Mockito.mock(NodeTraversal.class);
 PowerMockito.when(nodeTraversal.getParent()).thenReturn(rootNode);
 PowerMockito.when(nodeTraversal.traverseTopDown(Mockito.any(Node.class),
 Mockito.any(ScopedCallback.class))).thenReturn(null);
 PowerMockito.when(nodeTraversal.visit(Mockito.any(NodeTraversal.class),
 Mockito.any(Node.class), Mockito.any(Node.class))).thenReturn(null);

 ScopedCallback scopedCallback = new RenameLabels.ProcessLabels();
 scopedCallback.visit(nodeTraversal, labelNode, rootNode);

 String expectedName = "a";
 String newName = "renamedLabel";
 String labelName = labelNode.getFirstChild().getString();
 Preconditions.checkState(labelName.length() != 0);

 PowerMockito.verify(labelNode).setString(newName);
 PowerMockito.verify(mockCompiler).reportCodeChange();

 assertEquals(expectedName, labelName);
 }

 @Test
 public void testVisitLabelWithExistingName() {
 RenameLabels renameLabels = new RenameLabels(mockCompiler);

 Node labelNode = Mockito.mock(Node.class);
 PowerMockito.when(labelNode.getFirstChild()).thenReturn(Mockito.mock(Node.class));
 PowerMockito.when(labelNode.getLastChild()).thenReturn(Mockito.mock(Node.class));
 PowerMockito.when(labelNode.getType()).thenReturn(Token.LABEL);
 PowerMockito.when(labelNode.getParent()).thenReturn(rootNode);
 PowerMockito.when(labelNode.getFirstChild().getString()).thenReturn("a");
 PowerMockito.when(mockCompiler.reportCodeChange()).thenReturn(null);
 PowerMockito.when(rootNode.getFirstChild()).thenReturn(labelNode);

 ScopedCallback scopedCallback = new RenameLabels.ProcessLabels();
 renameLabels.process(null, rootNode);

 String existingName = "a";
 String labelName = labelNode.getFirstChild().getString();

 PowerMockito.verifyZeroInteractions(labelNode);
 PowerMockito.verifyZeroInteractions(mockCompiler);

 assertEquals(existingName, labelName);
 }

 @Test
 public void testVisitLabelWithNoParent() {
 RenameLabels renameLabels = new RenameLabels(mockCompiler);

 Node labelNode = Mockito.mock(Node.class);
 PowerMockito.when(labelNode.getFirstChild()).thenReturn(Mockito.mock(Node.class));
 PowerMockito.when(labelNode.getLastChild()).thenReturn(Mockito.mock(Node.class));
 PowerMockito.when(labelNode.getType()).thenReturn(Token.LABEL);
 PowerMockito.when(labelNode.getParent()).thenReturn(null);
 PowerMockito.when(mockCompiler.reportCodeChange()).thenReturn(null);

 ScopedCallback scopedCallback = new RenameLabels.ProcessLabels();
 renameLabels.process(null, rootNode);

 PowerMockito.verifyZeroInteractions(labelNode);
 PowerMockito.verifyZeroInteractions(mockCompiler);
 }

 @Test
 public void testVisitBreakOrContinueWithNewName() {
 RenameLabels renameLabels = new RenameLabels(mockCompiler);

 Node breakNode = Mockito.mock(Node.class);
 PowerMockito.when(breakNode.getFirstChild()).thenReturn(Mockito.mock(Node.class));
 PowerMockito.when(breakNode.getType()).thenReturn(Token.BREAK);
 PowerMockito.when(breakNode.getParent()).thenReturn(rootNode);
 PowerMockito.when(breakNode.getFirstChild().getString()).thenReturn("a");
 PowerMockito.when(mockCompiler.reportCodeChange()).thenReturn(null);

 NodeTraversal nodeTraversal = Mockito.mock(NodeTraversal.class);
 PowerMockito.when(nodeTraversal.getParent()).thenReturn(rootNode);
 PowerMockito.when(nodeTraversal.traverseTopDown(Mockito.any(Node.class),
 Mockito.any(ScopedCallback.class))).thenReturn(null);
 PowerMockito.when(nodeTraversal.visit(Mockito.any(NodeTraversal.class),
 Mockito.any(Node.class), Mockito.any(Node.class))).thenReturn(null);

 ScopedCallback scopedCallback = new RenameLabels.ProcessLabels();
 scopedCallback.visitBreakOrContinue(breakNode);

 String expectedName = "a";
 String newName = "renamedLabel";
 String labelName = breakNode.getFirstChild().getString();
 Preconditions.checkState(labelName.length() != 0);

 PowerMockito.verify(breakNode).setString(newName);
 PowerMockito.verify(mockCompiler).reportCodeChange();

 assertEquals(expectedName, labelName);
 }

 @Test
 public void testVisitBreakOrContinueWithExistingName() {
 RenameLabels renameLabels = new RenameLabels(mockCompiler);

 Node breakNode = Mockito.mock(Node.class);
 PowerMockito.when(breakNode.getFirstChild()).thenReturn(Mockito.mock(Node.class));
 PowerMockito.when(breakNode.getType()).thenReturn(Token.BREAK);
 PowerMockito.when(breakNode.getParent()).thenReturn(rootNode);
 PowerMockito.when(breakNode.getFirstChild().getString()).thenReturn("a");
 PowerMockito.when(mockCompiler.reportCodeChange()).thenReturn(null);
 PowerMockito.when(rootNode.getFirstChild()).thenReturn(breakNode);

 ScopedCallback scopedCallback = new RenameLabels.ProcessLabels();
 renameLabels.process(null, rootNode);

 String existingName = "a";
 String labelName = breakNode.getFirstChild().getString();

 PowerMockito.verifyZeroInteractions(breakNode);
 PowerMockito.verifyZeroInteractions(mockCompiler);

 assertEquals(existingName, labelName);
 }

 @Test
 public void testVisitBreakOrContinueWithNoParent() {
 RenameLabels renameLabels = new RenameLabels(mockCompiler);

 Node breakNode = Mockito.mock(Node.class);
 PowerMockito.when(breakNode.getFirstChild()).thenReturn(Mockito.mock(Node.class));
 PowerMockito.when(breakNode.getType()).thenReturn(Token.BREAK);
 PowerMockito.when(breakNode.getParent()).thenReturn(null);
 PowerMockito.when(mockCompiler.reportCodeChange()).thenReturn(null);

 ScopedCallback scopedCallback = new RenameLabels.ProcessLabels();
 renameLabels.process(null, rootNode);

 PowerMockito.verifyZeroInteractions(breakNode);
 PowerMockito.verifyZeroInteractions(mockCompiler);
 }

 @Test
 public void testRemoveUnusedLabel() {
 RenameLabels renameLabels = new RenameLabels(mockCompiler, null, false);

 Node labelNode = Mockito.mock(Node.class);
 PowerMockito.when(labelNode.getFirstChild()).thenReturn(Mockito.mock(Node.class));
 PowerMockito.when(labelNode.getLastChild()).thenReturn(Mockito.mock(Node.class));
 PowerMockito.when(labelNode.getType()).thenReturn(Token.LABEL);
 PowerMockito.when(labelNode.getParent()).thenReturn(rootNode);
 PowerMockito.when(mockCompiler.reportCodeChange()).thenReturn(null);
 PowerMockito.when(rootNode.getFirstChild()).thenReturn(labelNode);

 ScopedCallback scopedCallback = new RenameLabels.ProcessLabels();
 renameLabels.process(null, rootNode);

 PowerMockito.verifyZeroInteractions(labelNode);
 PowerMockito.verifyZeroInteractions(mockCompiler);

 assertNull(rootNode.getFirstChild());
 }

 @Test
 public void testRemoveUnusedLabelWithParent() {
 RenameLabels renameLabels = new RenameLabels(mockCompiler, null, false);

 Node labelNode = Mockito.mock(Node.class);
 Node parentNode = Mockito.mock(Node.class);
 PowerMockito.when(labelNode.getFirstChild()).thenReturn(Mockito.mock(Node.class));
 PowerMockito.when(labelNode.getLastChild()).thenReturn(Mockito.mock(Node.class));
 PowerMockito.when(labelNode.getType()).thenReturn(Token.LABEL);
 PowerMockito.when(labelNode.getParent()).thenReturn(parentNode);
 PowerMockito.when(parentNode.getFirstChild()).thenReturn(labelNode);
 PowerMockito.when(parentNode.getLastChild()).thenReturn(Mockito.mock(Node.class));
 PowerMockito.when(parentNode.getParent()).thenReturn(rootNode);
 PowerMockito.when(mockCompiler.reportCodeChange()).thenReturn(null);

 ScopedCallback scopedCallback = new RenameLabels.ProcessLabels();
 renameLabels.process(null, rootNode);

 PowerMockito.verify(labelNode).remove();
 PowerMockito.verify(parentNode).replaceChild(Mockito.any(Node.class),
 Mockito.same(labelNode.getLastChild()));
 PowerMockito.verifyZeroInteractions(mockCompiler);
 }

 @Test
 public void testGetLabelInfo() {
 RenameLabels renameLabels = new RenameLabels(mockCompiler);

 LabelNamespace labelNamespace = new LabelNamespace();
 LabelInfo labelInfo = new LabelInfo(1);
 labelNamespace.renameMap.put("a", labelInfo);

 ScopedCallback scopedCallback = new RenameLabels.ProcessLabels();
 renameLabels.process(null, rootNode);

 LabelInfo result = scopedCallback.getLabelInfo("a");

 assertEquals(labelInfo, result);
 }

 @Test
 public void testGetLabelInfoNull() {
 RenameLabels renameLabels = new RenameLabels(mockCompiler);

 ScopedCallback scopedCallback = new RenameLabels.ProcessLabels();
 renameLabels.process(null, rootNode);

 LabelInfo result = scopedCallback.getLabelInfo(null);

 assertNull(result);
 }

 @Test
 public void testGenerateNameForId() {
 RenameLabels renameLabels = new RenameLabels(mockCompiler);

 String expectedName = "a";
 String result = renameLabels.new ProcessLabels().getNameForId(1);

 assertEquals(expectedName, result);
 }

 @Test
 public void testGenerateNameForIdWithExistingNames() {
 RenameLabels renameLabels = new RenameLabels(mockCompiler);

 ArrayList<String> names = Lists.newArrayList("a", "b", "c");
 LabelNamespace labelNamespace = new LabelNamespace();
 labelNamespace.renameMap.put("a", new LabelInfo(1));
 labelNamespace.renameMap.put("b", new LabelInfo(2));
 labelNamespace.renameMap.put("c", new LabelInfo(3));

 ScopedCallback scopedCallback = new RenameLabels.ProcessLabels();
 renameLabels.process(null, rootNode);

 String expectedName = "d";
 String result = scopedCallback.getNameForId(4);

 assertEquals(expectedName, result);
 }
}