package org.objectweb.asm.jip.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.objectweb.asm.Type;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.jip.Type;
import org.objectweb.asm.jip.MethodAdapter;
import org.objectweb.asm.jip.commons.LocalVariablesSorter;

public class LocalVariablesSorterTest {
    
    @Mock
    private MethodVisitor mv;
    
    private LocalVariablesSorter lvs;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        lvs = new LocalVariablesSorter(1, "()V", mv);
    }
    
    @Test
    public void testConstructor() {
        assertEquals(40, lvs.mapping.length);
        assertEquals(20, lvs.newLocals.length);
        assertEquals(1, lvs.nextLocal);
        assertEquals(0, lvs.firstLocal);
    }
    
    @Test
    public void testVisitVarInsn() {
        lvs.visitVarInsn(21, 0);
        verify(mv, times(1)).visitVarInsn(21, 0);
        
        lvs.visitVarInsn(54, 1);
        verify(mv, times(1)).visitVarInsn(54, 1);
        
        lvs.visitVarInsn(23, 2);
        verify(mv, times(1)).visitVarInsn(23, 2);
        
        lvs.visitVarInsn(57, 3);
        verify(mv, times(1)).visitVarInsn(57, 3);
    }
    
    @Test
    public void testVisitIincInsn() {
        lvs.visitIincInsn(0, 1);
        verify(mv, times(1)).visitIincInsn(0, 1);
        
        lvs.visitIincInsn(1, 2);
        verify(mv, times(1)).visitIincInsn(1, 2);
        
        lvs.visitIincInsn(2, 3);
        verify(mv, times(1)).visitIincInsn(2, 3);
        
        lvs.visitIincInsn(3, 4);
        verify(mv, times(1)).visitIincInsn(3, 4);
    }
    
    @Test
    public void testVisitMaxs() {
        lvs.visitMaxs(0, 1);
        verify(mv, times(1)).visitMaxs(0, 1);
        
        lvs.visitMaxs(2, 3);
        verify(mv, times(1)).visitMaxs(2, 3);
        
        lvs.visitMaxs(4, 5);
        verify(mv, times(1)).visitMaxs(4, 5);
        
        lvs.visitMaxs(6, 7);
        verify(mv, times(1)).visitMaxs(6, 7);
    }
    
    @Test
    public void testVisitLocalVariable() {
        lvs.visitLocalVariable("name", "desc", "signature", null, null, 0);
        verify(mv, times(1)).visitLocalVariable("name", "desc", "signature", null, null, 0);
        
        lvs.visitLocalVariable("name", "desc", "signature", null, null, 1);
        verify(mv, times(1)).visitLocalVariable("name", "desc", "signature", null, null, 1);
        
        lvs.visitLocalVariable("name", "desc", "signature", null, null, 2);
        verify(mv, times(1)).visitLocalVariable("name", "desc", "signature", null, null, 2);
        
        lvs.visitLocalVariable("name", "desc", "signature", null, null, 3);
        verify(mv, times(1)).visitLocalVariable("name", "desc", "signature", null, null, 3);
    }
    
    @Test
    public void testVisitFrame() {
        lvs.visitFrame(-1, 1, null, 1, null);
        verify(mv, times(1)).visitFrame(-1, 1, null, 1, null);
        
        lvs.visitFrame(0, 2, null, 2, null);
        verify(mv, times(1)).visitFrame(0, 2, null, 2, null);
        
        lvs.visitFrame(1, 3, null, 3, null);
        verify(mv, times(1)).visitFrame(1, 3, null, 3, null);
        
        lvs.visitFrame(2, 4, null, 4, null);
        verify(mv, times(1)).visitFrame(2, 4, null, 4, null);
    }
    
    @Test
    public void testNewLocal() {
        int result = lvs.newLocal(Type.INT_TYPE);
        assertEquals(1, result);
        assertEquals(2, lvs.nextLocal);
        
        result = lvs.newLocal(Type.FLOAT_TYPE);
        assertEquals(2, result);
        assertEquals(3, lvs.nextLocal);
        
        result = lvs.newLocal(Type.LONG_TYPE);
        assertEquals(3, result);
        assertEquals(5, lvs.nextLocal);
        
        result = lvs.newLocal(Type.DOUBLE_TYPE);
        assertEquals(5, result);
        assertEquals(7, lvs.nextLocal);
        
        result = lvs.newLocal(Type.getObjectType("TestClass"));
        assertEquals(7, result);
        assertEquals(8, lvs.nextLocal);
    }
    
    @Test
    public void testSetLocalType() {
        lvs.setLocalType(0, Type.INT_TYPE);
        lvs.setLocalType(1, Type.DOUBLE_TYPE);
        lvs.setLocalType(2, Type.getObjectType("TestClass"));
    }
    
    @Test
    public void testSetFrameLocal() {
        lvs.setFrameLocal(0, Opcodes.INTEGER);
        lvs.setFrameLocal(1, Opcodes.DOUBLE);
        lvs.setFrameLocal(2, Opcodes.FLOAT);
        
        assertEquals(3, lvs.newLocals.length);
        assertEquals(Opcodes.INTEGER, lvs.newLocals[0]);
        assertEquals(Opcodes.DOUBLE, lvs.newLocals[1]);
        assertEquals(Opcodes.FLOAT, lvs.newLocals[2]);
    }
    
    @Test
    public void testRemap() {
        int var = lvs.remap(0, Type.INT_TYPE);
        assertEquals(0, var);
        assertTrue(lvs.changed);
        
        lvs.changed = false;
        var = lvs.remap(1, Type.DOUBLE_TYPE);
        assertEquals(1, var);
        assertTrue(lvs.changed);
        
        lvs.changed = false;
        var = lvs.remap(2, Type.FLOAT_TYPE);
        assertEquals(2, var);
        assertTrue(lvs.changed);
        
        lvs.changed = false;
        var = lvs.remap(3, Type.LONG_TYPE);
        assertEquals(4, var);
        assertTrue(lvs.changed);
        
        lvs.changed = false;
        var = lvs.remap(4, Type.getObjectType("TestClass"));
        assertEquals(5, var);
        assertTrue(lvs.changed);
        
        lvs.changed = false;
        var = lvs.remap(5, Type.INT_TYPE);
        assertEquals(0, var);
        assertFalse(lvs.changed);
        
        lvs.changed = false;
        var = lvs.remap(6, Type.DOUBLE_TYPE);
        assertEquals(1, var);
        assertFalse(lvs.changed);
        
        lvs.changed = false;
        var = lvs.remap(7, Type.FLOAT_TYPE);
        assertEquals(2, var);
        assertFalse(lvs.changed);
        
        lvs.changed = false;
        var = lvs.remap(8, Type.LONG_TYPE);
        assertEquals(4, var);
        assertFalse(lvs.changed);
        
        lvs.changed = false;
        var = lvs.remap(9, Type.getObjectType("TestClass"));
        assertEquals(5, var);
        assertFalse(lvs.changed);
    }
    
    @Test
    public void testNewLocalMapping() {
        int result = lvs.newLocalMapping(Type.INT_TYPE);
        assertEquals(1, result);
        assertEquals(2, lvs.nextLocal);
        
        result = lvs.newLocalMapping(Type.FLOAT_TYPE);
        assertEquals(2, result);
        assertEquals(3, lvs.nextLocal);
        
        result = lvs.newLocalMapping(Type.LONG_TYPE);
        assertEquals(3, result);
        assertEquals(5, lvs.nextLocal);
        
        result = lvs.newLocalMapping(Type.DOUBLE_TYPE);
        assertEquals(5, result);
        assertEquals(7, lvs.nextLocal);
        
        result = lvs.newLocalMapping(Type.getObjectType("TestClass"));
        assertEquals(7, result);
        assertEquals(8, lvs.nextLocal);
    }
}