package org.objectweb.asm.jip.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.jip.commons.LocalVariablesSorter;
import org.objectweb.asm.jip.Type;

public class LocalVariablesSorterTest {

    private LocalVariablesSorter sorter;
    
    @Before
    public void setUp() {
        sorter = new LocalVariablesSorter(1, "desc", null);
    }
    
    @Test
    public void testVisitVarInsn() {
        sorter.visitVarInsn(22, 1);
        assertEquals(1, sorter.remap(1, Type.LONG_TYPE));
        
        sorter.visitVarInsn(55, 2);
        assertEquals(2, sorter.remap(2, Type.LONG_TYPE));
        
        sorter.visitVarInsn(24, 3);
        assertEquals(3, sorter.remap(3, Type.DOUBLE_TYPE));
        
        sorter.visitVarInsn(57, 4);
        assertEquals(4, sorter.remap(4, Type.DOUBLE_TYPE));
        
        sorter.visitVarInsn(23, 5);
        assertEquals(5, sorter.remap(5, Type.FLOAT_TYPE));
        
        sorter.visitVarInsn(56, 6);
        assertEquals(6, sorter.remap(6, Type.FLOAT_TYPE));
        
        sorter.visitVarInsn(21, 7);
        assertEquals(7, sorter.remap(7, Type.INT_TYPE));
        
        sorter.visitVarInsn(54, 8);
        assertEquals(8, sorter.remap(8, Type.INT_TYPE));
        
        sorter.visitVarInsn(100, 9);
        assertEquals(9, sorter.remap(9, LocalVariablesSorter.OBJECT_TYPE));
    }

    @Test
    public void testVisitIincInsn() {
        sorter.visitIincInsn(1, 5);
        assertEquals(1, sorter.remap(1, Type.INT_TYPE));
        
        sorter.visitIincInsn(2, 10);
        assertEquals(2, sorter.remap(2, Type.INT_TYPE));
    }

    @Test(expected=IllegalStateException.class)
    public void testVisitMaxs() {
        sorter.visitMaxs(10, 20);
        assertEquals(20, sorter.nextLocal);
    }

    @Test
    public void testVisitLocalVariable() {
        sorter.visitLocalVariable("name", "desc", "signature", null, null, 1);
        assertEquals(1, sorter.remap(1, Type.getType("desc")));
    }
    
    @Test(expected=IllegalStateException.class)
    public void testVisitFrame() {
        Object[] local = new Object[] {Opcodes.INTEGER, Opcodes.INTEGER};
        Object[] stack = new Object[] {Opcodes.FLOAT, Opcodes.DOUBLE};
        sorter.visitFrame(-1, 2, local, 2, stack);
        assertEquals(2, sorter.newLocals.length);
    }
    
    @Test
    public void testNewLocal() {
        int local = sorter.newLocal(Type.INT_TYPE);
        assertEquals(1, sorter.nextLocal);
        assertEquals(1, sorter.remap(local, Type.INT_TYPE));
        
        local = sorter.newLocal(Type.FLOAT_TYPE);
        assertEquals(2, sorter.nextLocal);
        assertEquals(2, sorter.remap(local, Type.FLOAT_TYPE));
        
        local = sorter.newLocal(Type.LONG_TYPE);
        assertEquals(4, sorter.nextLocal);
        assertEquals(4, sorter.remap(local, Type.LONG_TYPE));
        
        local = sorter.newLocal(Type.DOUBLE_TYPE);
        assertEquals(6, sorter.nextLocal);
        assertEquals(6, sorter.remap(local, Type.DOUBLE_TYPE));
        
        local = sorter.newLocal(Type.getObjectType("java/lang/Object"));
        assertEquals(8, sorter.nextLocal);
        assertEquals(8, sorter.remap(local, LocalVariablesSorter.OBJECT_TYPE));
    }
    
    @Test
    public void testSetLocalType() {
        int local = 1;
        Type type = Type.INT_TYPE;
        sorter.setLocalType(local, type);
        // Test implementation
      
    }
    
    @Test
    public void testSetFrameLocal() {
        int local = 1;
        Object type = Opcodes.INTEGER;
        sorter.setFrameLocal(local, type);
        // Test implementation
    }
    
    @Test
    public void testRemap() {
        int var = 1;
        Type type = Type.INT_TYPE;
        int result = sorter.remap(var, type);
        assertEquals(1, result);
        
        var = 0;
        type = Type.LONG_TYPE;
        result = sorter.remap(var, type);
        assertEquals(0, result);
        
        var = 2;
        type = Type.DOUBLE_TYPE;
        result = sorter.remap(var, type);
        assertEquals(2, result);
        
        var = 3;
        type = Type.FLOAT_TYPE;
        result = sorter.remap(var, type);
        assertEquals(3, result);
        
        var = 4;
        type = Type.OBJECT_TYPE;
        result = sorter.remap(var, type);
        assertEquals(4, result);
        
        var = 5;
        type = Type.INT_TYPE;
        result = sorter.remap(var, type);
        assertEquals(5, result);
    }

    @Test
    public void testNewLocalMapping() {
        Type type = Type.INT_TYPE;
        int result = sorter.newLocalMapping(type);
        assertEquals(1, result);
        
        type = Type.LONG_TYPE;
        result = sorter.newLocalMapping(type);
        assertEquals(2, result);
        
        type = Type.DOUBLE_TYPE;
        result = sorter.newLocalMapping(type);
        assertEquals(3, result);
        
        type = Type.FLOAT_TYPE;
        result = sorter.newLocalMapping(type);
        assertEquals(4, result);
        
        type = Type.getObjectType("java/lang/Object");
        result = sorter.newLocalMapping(type);
        assertEquals(5, result);
    }
}