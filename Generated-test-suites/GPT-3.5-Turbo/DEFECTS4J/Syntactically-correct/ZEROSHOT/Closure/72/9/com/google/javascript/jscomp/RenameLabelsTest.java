import com.google.common.base.Supplier;
import java.util.Deque;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class RenameLabelsTest {

  private AbstractCompiler compiler;
  private Supplier<String> nameSupplier;
  private boolean removeUnused;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    nameSupplier = mock(Supplier.class);
    removeUnused = true;
  }

  @Test
  public void testConstructorWithCompiler() {
    RenameLabels renameLabels = new RenameLabels(compiler);
  }

  @Test
  public void testConstructorWithCompilerSupplierAndRemoveUnused() {
    RenameLabels renameLabels = new RenameLabels(compiler, nameSupplier, removeUnused);
  }

  @Test
  public void testProcess() {
    RenameLabels renameLabels = new RenameLabels(compiler, nameSupplier, removeUnused);
    Node externs = mock(Node.class);
    Node root = mock(Node.class);
    renameLabels.process(externs, root);
  }

  @Test
  public void testProcessLabelsEnterScope() {
    RenameLabels.ProcessLabels processLabels = new RenameLabels(compiler).new ProcessLabels();
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    processLabels.enterScope(nodeTraversal);
  }

  @Test
  public void testProcessLabelsExitScope() {
    RenameLabels.ProcessLabels processLabels = new RenameLabels(compiler).new ProcessLabels();
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    processLabels.exitScope(nodeTraversal);
  }

  @Test
  public void testShouldTraverse() {
    RenameLabels.ProcessLabels processLabels = new RenameLabels(compiler).new ProcessLabels();
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    Node node = mock(Node.class);
    Node parent = mock(Node.class);
    processLabels.shouldTraverse(nodeTraversal, node, parent);
  }

  @Test
  public void testVisitLabel() {
    RenameLabels.ProcessLabels processLabels = new RenameLabels(compiler).new ProcessLabels();
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    Node node = mock(Node.class);
    Node parent = mock(Node.class);
    processLabels.visitLabel(node, parent);
  }

  @Test
  public void testVisitBreakOrContinue() {
    RenameLabels.ProcessLabels processLabels = new RenameLabels(compiler).new ProcessLabels();
    Node node = mock(Node.class);
    processLabels.visitBreakOrContinue(node);
  }

  @Test
  public void testGetNameForId() {
    RenameLabels.ProcessLabels processLabels = new RenameLabels(compiler).new ProcessLabels();
    int id = 1;
    String result = processLabels.getNameForId(id);
  }

  @Test
  public void testGetLabelInfo() {
    RenameLabels.ProcessLabels processLabels = new RenameLabels(compiler).new ProcessLabels();
    String name = "test";
    RenameLabels.LabelInfo result = processLabels.getLabelInfo(name);
  }
}