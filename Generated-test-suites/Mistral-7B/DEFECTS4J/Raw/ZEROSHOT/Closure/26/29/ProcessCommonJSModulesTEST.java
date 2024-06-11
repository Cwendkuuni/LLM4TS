import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.SourceName;

import java.io.File;
import java.util.Set;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ProcessCommonJSModules.class})
public class ProcessCommonJSModulesTest {

 private AbstractCompiler compiler;
 private NodeFactory nodeFactory;
 private CompilerPassManager passManager;
 private Node root;
 private SourceName sourceName;

 @Before
 public void setUp() {
 compiler = Mockito.mock(AbstractCompiler.class);
 nodeFactory = Mockito.mock(NodeFactory.class);
 passManager = new CompilerPassManager(compiler, nodeFactory);
 root = Node.root();
 sourceName = new SourceName("test.js");
 }

 @Test
 public void testProcessCommonJSModules() throws Exception {
 ProcessCommonJSModules ccjm = new ProcessCommonJSModules(compiler, "");
 ccjm.process(null, root);
 Assert.assertNull(ccjm.getModule());

 ProcessCommonJSModules ccjm2 = new ProcessCommonJSModules(compiler, "", false);
 ccjm2.process(null, root);
 Assert.assertNotNull(ccjm2.getModule());
 }

 @Test
 public void testGuessCJSModuleName() throws Exception {
 ProcessCommonJSModules ccjm = new ProcessCommonJSModules(compiler, "");
 Assert.assertEquals("module$test", ccjm.guessCJSModuleName("test.js"));
 Assert.assertEquals("module$test$subdir$file", ccjm.guessCJSModuleName("subdir/file.js"));
 }

 @Test
 public void testToModuleName() throws Exception {
 Assert.assertEquals("module$test", ProcessCommonJSModules.toModuleName("test.js"));
 Assert.assertEquals("module$test$subdir$file", ProcessCommonJSModules.toModuleName("subdir/file.js"));
 Assert.assertEquals("module$test$./subdir/file", ProcessCommonJSModules.toModuleName("test.js", "subdir/file.js"));
 }

 @Test
 public void testNormalizeSourceName() throws Exception {
 ProcessCommonJSModules ccjm = new ProcessCommonJSModules(compiler, "");
 Assert.assertEquals("test.js", ccjm.normalizeSourceName("test.js"));
 Assert.assertEquals("subdir/file.js", ccjm.normalizeSourceName("subdir/file.js"));
 Assert.assertEquals("test", ccjm.normalizeSourceName("test"));
 }

 @Test
 public void testProcess() throws Exception {
 ProcessCommonJSModules ccjm = new ProcessCommonJSModules(compiler, "");
 NodeTraversal traversal = new NodeTraversal(passManager, root, sourceName);
 ccjm.process(null, root);
 Traversal t = PowerMockito.mock(NodeTraversal.class);
 PowerMockito.whenNew(NodeTraversal.class).withArguments(passManager, root, sourceName).thenReturn(t);
 PowerMockito.when(t.getInput()).thenReturn(SourceName.getBuiltinSourceName("test.js"));
 PowerMockito.when(t.getSourceName()).thenReturn(sourceName);

 ccjm.process(null, root);
 Node requireCall = root.getFirstChild().getFirstChild();
 Node requireModuleName = requireCall.getChildAtIndex(1);
 Node moduleRef = requireCall.getChildAtIndex(2);
 Node moduleName = moduleRef.getParent().getFirstChild();
 Node script = root.getChildAtIndex(1);
 Node googProvideCall = script.getFirstChild();
 Node googProvideModuleName = googProvideCall.getChildAtIndex(1);
 Node googRequireCall = script.getLastChild();
 Node googRequireModuleName = googRequireCall.getChildAtIndex(1);
 Node moduleExports = script.getChildAtIndex(2);

 Assert.assertEquals("module$test", requireModuleName.getString());
 Assert.assertEquals("module$test", moduleName.getString());
 Assert.assertEquals("goog.provide(\"module$test\");", googProvideCall.toString());
 Assert.assertEquals("goog.require(\"module$test\");", googRequireCall.toString());
 Assert.assertEquals("exports", moduleExports.getString());
 }
}