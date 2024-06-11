package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class RenameLabelsTest {
    
    private RenameLabels renamer;
    private AbstractCompiler compiler;
    
    @Test
    public void testProcess() {
        renamer = new RenameLabels(compiler);
        renamer.process(null, null);
    }
    
    @Test
    public void testProcessWithNameSupplier() {
        renamer = new RenameLabels(compiler, new RenameLabels.DefaultNameSupplier(), true);
        renamer.process(null, null);
    }
    
    @Test
    public void testProcessShouldTraverse() {
        RuleSetter rs = new RuleSetter();
        rs.setRemoveUnused(true);
        renamer = new RenameLabels(compiler, new RenameLabels.DefaultNameSupplier(), rs.getRemoveUnused());
        renamer.process(null, null);
    }
    
    @Test
    public void testProcessShouldTraverseWithNameSupplier() {
        RuleSetter rs = new RuleSetter();
        rs.setRemoveUnused(true);
        renamer = new RenameLabels(compiler, new RenameLabels.DefaultNameSupplier(), rs.getRemoveUnused());
        renamer.process(null, null);
    }
    
    @Test
    public void testProcessShouldTraverseWithProcessedCallback() {
        RuleSetter rs = new RuleSetter();
        rs.setRemoveUnused(true);
        renamer = new RenameLabels(compiler, new RenameLabels.DefaultNameSupplier(), rs.getRemoveUnused());
        renamer.process(null, null);
    }
    
    @Test
    public void testEnterScope() {
        ProcessLabels processLabels = renamer.new ProcessLabels();
        processLabels.enterScope(null);
    }
    
   @Test
    public void testExitScope() {
        ProcessLabels processLabels = renamer.new ProcessLabels();
        processLabels.exitScope(null);
    }
    
    @Test
    public void testShouldTraverse() {
        ProcessLabels processLabels = renamer.new ProcessLabels();
        assertEquals(true, processLabels.shouldTraverse(null, null, null));
    }
    
    @Test
    public void testVisit() {
        ProcessLabels processLabels = renamer.new ProcessLabels();
        processLabels.visit(null, null, null);
    }
     
    @Test
    public void testGetInstanceExposed() {
        RenameLabels.DefaultNameSupplier dns = new RenameLabels.DefaultNameSupplier();
        dns.get();
    }
    
    @Test
    public void testEnterScopeExposed() {
        ProcessLabels processLabels = renamer.new ProcessLabels();
        NodeTraversal nt = new NodeTraversal(compiler, processLabels);
        processLabels.enterScope(nt);
    }
    
    @Test
    public void testExitScopeExposed() {
        ProcessLabels processLabels = renamer.new ProcessLabels();
        NodeTraversal nt = new NodeTraversal(compiler, processLabels);
        processLabels.exitScope(nt);
    }
    
    @Test
    public void testShouldTraverseExposed() {
        ProcessLabels processLabels = renamer.new ProcessLabels();
        NodeTraversal nt = new NodeTraversal(compiler, processLabels);
        assertEquals(true, processLabels.shouldTraverse(nt, null, null));
    }
    
    @Test
    public void testVisitExposed() {
        ProcessLabels processLabels = renamer.new ProcessLabels();
        NodeTraversal nt = new NodeTraversal(compiler, processLabels);
        processLabels.visit(nt, null, null);
    }
    
    @Test
    public void testVisitBreakOrContinue() {
        ProcessLabels processLabels = renamer.new ProcessLabels();
        processLabels.visitBreakOrContinue(null);
    }
    
    @Test
    public void testVisitLabel() {
        ProcessLabels processLabels = renamer.new ProcessLabels();
        processLabels.visitLabel(null, null);
    }
    
    @Test
    public void testGetNameForId() {
        ProcessLabels processLabels = renamer.new ProcessLabels();
        assertEquals("a", processLabels.getNameForId(1));
    }
    
    @Test
    public void testGetLabelInfo() {
        ProcessLabels processLabels = renamer.new ProcessLabels();
        assertNull(processLabels.getLabelInfo("name"));
    }
    
    @Test
    public void testNewLabelInfo() {
        RenameLabels.LabelInfo li = new RenameLabels.LabelInfo(1);
        assertEquals(false, li.referenced);
        assertEquals(1, li.id);
    }
    
    @Test
    public void testNewLabelNamespace() {
        RenameLabels.LabelNamespace namespace = new RenameLabels.LabelNamespace();
        assertEquals(0, namespace.renameMap.size());
    }
}