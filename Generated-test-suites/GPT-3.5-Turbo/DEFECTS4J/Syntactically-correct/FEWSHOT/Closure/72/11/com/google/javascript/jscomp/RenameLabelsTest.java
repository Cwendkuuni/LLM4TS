import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.google.common.base.Supplier;
import com.google.javascript.jscomp.LabelNamespace;
import com.google.javascript.jscomp.RenameLabels.DefaultNameSupplier;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class RenameLabelsTest {

    private RenameLabels renameLabels;
    private AbstractCompiler compiler;
    private Supplier<String> nameSupplier;
    private boolean removeUnused;

    @Before
    public void setUp() {
        compiler = mock(AbstractCompiler.class);
        nameSupplier = mock(Supplier.class);
        removeUnused = true;
        renameLabels = new RenameLabels(compiler, nameSupplier, removeUnused);
    }

    @Test
    public void testConstructor() {
        assertEquals(compiler, renameLabels.compiler);
        assertEquals(nameSupplier, renameLabels.nameSupplier);
        assertEquals(removeUnused, renameLabels.removeUnused);
    }

    @Test
    public void testProcess() {
        Node externs = mock(Node.class);
        Node root = mock(Node.class);

        NodeTraversal.traverse(compiler, root, new renameLabels.ProcessLabels());

        verify(compiler, times(1)).reportCodeChange();
    }

    @Test
    public void testGetNameForId() {
        ArrayList<String> names = new ArrayList<String>();
        names.add("a");
        names.add("b");
        names.add("c");
        renameLabels.ProcessLabels pl = renameLabels.new ProcessLabels();
        pl.names = names;

        assertEquals("a", pl.getNameForId(1));
        assertEquals("b", pl.getNameForId(2));
        assertEquals("c", pl.getNameForId(3));
    }

    @Test
    public void testGetLabelInfo() {
        renameLabels.ProcessLabels pl = renameLabels.new ProcessLabels();
        LabelNamespace renameMap = new LabelNamespace();
        LabelNamespace mockNamespace = spy(renameMap);
        pl.namespaceStack.push(mockNamespace);

        LabelInfo labelInfo = new LabelInfo(1);
        labelInfo.referenced = true;
        renameMap.renameMap.put("a", labelInfo);
        when(mockNamespace.renameMap).thenReturn(renameMap.renameMap);

        assertEquals(labelInfo, pl.getLabelInfo("a"));
        assertNull(pl.getLabelInfo("b"));
    }
}