
package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Supplier;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;

public class RenameLabelsTest {

    private RenameLabels renameLabels;
    private AbstractCompiler compiler;

    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
        renameLabels = new RenameLabels(compiler);
    }

    @Test
    public void testProcess() {
        Node externs = null;
        Node root = null;
        renameLabels.process(externs, root);
    }

    @Test
    public void testConstructor_withRemoveUnused_true() {
        renameLabels = new RenameLabels(compiler, new DefaultNameSupplier(), true);
        assertTrue(renameLabels.removeUnused);
    }

    @Test
    public void testConstructor_withRemoveUnused_false() {
        renameLabels = new RenameLabels(compiler, new DefaultNameSupplier(), false);
        assertFalse(renameLabels.removeUnused);
    }

    @Test
    public void testDefaultNameSupplierGet() {
        DefaultNameSupplier supplier = new DefaultNameSupplier();
        assertTrue(supplier.get().length() > 0);
    }

    @Test
    public void testProcessLabels() {
        RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
        NodeTraversal nodeTraversal = null;
        Node node = null;
        Node parent = null;
        assertTrue(processLabels.shouldTraverse(nodeTraversal, node, parent));
        processLabels.enterScope(nodeTraversal);
        processLabels.exitScope(nodeTraversal);
        processLabels.visit(nodeTraversal, node, parent);
        processLabels.visitBreakOrContinue(node);
        processLabels.visitLabel(node, parent);
        String name = "";
        assertNull(processLabels.getLabelInfo(name));
        assertEquals("replacedName", processLabels.getNameForId(1));
    }

    @Test
    public void testLabelInfoConstructor() {
        int id = 1;
        RenameLabels.LabelInfo labelInfo = new RenameLabels.LabelInfo(id);
        assertFalse(labelInfo.referenced);
        assertEquals(id, labelInfo.id);
    }

    @Test
    public void testLabelNamespaceConstructor() {
        RenameLabels.LabelNamespace labelNamespace = new RenameLabels.LabelNamespace();
        assertTrue(labelNamespace.renameMap.isEmpty());
    }
}
