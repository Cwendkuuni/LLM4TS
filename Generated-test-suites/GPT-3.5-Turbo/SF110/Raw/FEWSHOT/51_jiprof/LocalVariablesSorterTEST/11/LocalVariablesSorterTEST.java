package org.objectweb.asm.jip.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.jip.MethodVisitor;
import org.objectweb.asm.jip.Type;

public class LocalVariablesSorterTest {
    private MethodVisitor mv;
    private LocalVariablesSorter sorter;
    
    @Before
    public void setUp() {
        mv = mock(MethodVisitor.class);
        sorter = new LocalVariablesSorter(0, "", mv);
    }
    
    @Test
    public void testVisitVarInsn() {
        sorter.visitVarInsn(22, 1);
        verify(mv).visitVarInsn(22, 1);
        
        sorter.visitVarInsn(24, 2);
        verify(mv).visitVarInsn(24, 2);
        
        sorter.visitVarInsn(23, 3);
        verify(mv).visitVarInsn(23, 3);
        
        sorter.visitVarInsn(21, 4);
        verify(mv).visitVarInsn(21, 4);
        
        sorter.visitVarInsn(18, 5);
        verify(mv).visitVarInsn(18, 5);
    }
    
    @Test
    public void testVisitIincInsn() {
        sorter.visitIincInsn(1, 2);
        verify(mv).visitIincInsn(1, 2);
    }
    
    @Test
    public void testVisitMaxs() {
        sorter.visitMaxs(5, 10);
        verify(mv).visitMaxs(5, sorter.getNextLocal());
    }
    
    @Test
    public void testVisitLocalVariable() {
        sorter.visitLocalVariable("name", "desc", "signature", null, null, 1);
        verify(mv).visitLocalVariable("name", "desc", "signature", null, null, sorter.getRemap(1, Type.getType("desc")));
    }
    
    @Test
    public void testVisitFrame() {
        sorter.visitFrame(0, 5, new Object[5], 0, new Object[0]);
        verify(mv).visitFrame(0, sorter.getNewLocals().length, sorter.getNewLocals(), 0, new Object[0]);
    }
    
    @Test
    public void testNewLocal() {
        int local = sorter.newLocal(Type.INT_TYPE);
        assertEquals(sorter.getNextLocal(), local + Type.INT_TYPE.getSize());
        
        local = sorter.newLocal(Type.FLOAT_TYPE);
        assertEquals(sorter.getNextLocal(), local + Type.FLOAT_TYPE.getSize());
        
        local = sorter.newLocal(Type.LONG_TYPE);
        assertEquals(sorter.getNextLocal(), local + Type.LONG_TYPE.getSize());
        
        local = sorter.newLocal(Type.DOUBLE_TYPE);
        assertEquals(sorter.getNextLocal(), local + Type.DOUBLE_TYPE.getSize());
        
        local = sorter.newLocal(Type.getType("Ljava/lang/String;"));
        assertEquals(sorter.getNextLocal(), local + 1);
    }
    
    @Test
    public void testSetLocalType() {
        sorter.setLocalType(1, Type.INT_TYPE);
    }
    
    @Test
    public void testSetFrameLocal() {
        sorter.setFrameLocal(1, Opcodes.INTEGER);
    }
    
    @Test
    public void testRemap() {
        sorter.remap(1, Type.INT_TYPE);
        
        assertEquals(1, sorter.getRemap(1, Type.INT_TYPE));
    }
    
    @Test
    public void testNewLocalMapping() {
        int local = sorter.newLocalMapping(Type.INT_TYPE);
        assertEquals(sorter.getNextLocal(), local + Type.INT_TYPE.getSize());
    }
}