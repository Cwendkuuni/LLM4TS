package org.objectweb.asm.jip.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class LocalVariablesSorterTest {
    @Test
    public void testVisitVarInsn() {
        // Test for each opcode and variable number
        // Case 1
        LocalVariablesSorter sorter = new LocalVariablesSorter(1, "", null);
        sorter.visitVarInsn(21, 0);
        assertEquals(0, sorter.remap(0, sorter.OBJECT_TYPE));
        
        // Case 2
        sorter = new LocalVariablesSorter(1, "", null);
        sorter.visitVarInsn(22, 1);
        assertEquals(1, sorter.remap(1, sorter.OBJECT_TYPE));
        
        // Case 3
        sorter = new LocalVariablesSorter(1, "", null);
        sorter.visitVarInsn(23, 2);
        assertEquals(2, sorter.remap(2, sorter.OBJECT_TYPE));
        
        // Case 4
        sorter = new LocalVariablesSorter(1, "", null);
        sorter.visitVarInsn(24, 3);
        assertEquals(3, sorter.remap(3, sorter.OBJECT_TYPE));
        
        // Case 5
        sorter = new LocalVariablesSorter(1, "", null);
        sorter.visitVarInsn(55, 4);
        assertEquals(4, sorter.remap(4, sorter.OBJECT_TYPE));
        
        // Case 6
        sorter = new LocalVariablesSorter(1, "", null);
        sorter.visitVarInsn(56, 5);
        assertEquals(5, sorter.remap(5, sorter.OBJECT_TYPE));
        
        // Case 7
        sorter = new LocalVariablesSorter(1, "", null);
        sorter.visitVarInsn(57, 6);
        assertEquals(6, sorter.remap(6, sorter.OBJECT_TYPE));
    }
    
    @Test
    public void testVisitIincInsn() {
        // Test for each variable number and increment value
        // Case 1
        LocalVariablesSorter sorter = new LocalVariablesSorter(1, "", null);
        sorter.visitIincInsn(0, 1);
        assertEquals(0, sorter.remap(0, sorter.OBJECT_TYPE));
        
        // Case 2
        sorter = new LocalVariablesSorter(1, "", null);
        sorter.visitIincInsn(1, 2);
        assertEquals(1, sorter.remap(1, sorter.OBJECT_TYPE));
        
        // Case 3
        sorter = new LocalVariablesSorter(1, "", null);
        sorter.visitIincInsn(2, 3);
        assertEquals(2, sorter.remap(2, sorter.OBJECT_TYPE));
        
        // Case 4
        sorter = new LocalVariablesSorter(1, "", null);
        sorter.visitIincInsn(3, 4);
        assertEquals(3, sorter.remap(3, sorter.OBJECT_TYPE));
    }
    
    @Test
    public void testVisitMaxs() {
        // Test for different maxStack and maxLocals values
        // Case 1
        LocalVariablesSorter sorter = new LocalVariablesSorter(1, "", null);
        sorter.visitMaxs(10, 5);
        assertEquals(5, sorter.nextLocal);
        
        // Case 2
        sorter = new LocalVariablesSorter(1, "", null);
        sorter.visitMaxs(15, 7);
        assertEquals(7, sorter.nextLocal);
    }
    
    @Test
    public void testVisitLocalVariable() {
        // Test for different name, desc, signature, start, end, and index values
        // Case 1
        LocalVariablesSorter sorter = new LocalVariablesSorter(1, "", null);
        sorter.visitLocalVariable("var1", "int", null, null, null, 0);
        assertEquals(0, sorter.remap(0, sorter.OBJECT_TYPE));
        
        // Case 2
        sorter = new LocalVariablesSorter(1, "", null);
        sorter.visitLocalVariable("var2", "long", null, null, null, 1);
        assertEquals(1, sorter.remap(1, sorter.OBJECT_TYPE));
        
        // Case 3
        sorter = new LocalVariablesSorter(1, "", null);
        sorter.visitLocalVariable("var3", "float", null, null, null, 2);
        assertEquals(2, sorter.remap(2, sorter.OBJECT_TYPE));
        
        // Case 4
        sorter = new LocalVariablesSorter(1, "", null);
        sorter.visitLocalVariable("var4", "double", null, null, null, 3);
        assertEquals(3, sorter.remap(3, sorter.OBJECT_TYPE));
    }
    
    @Test
    public void testVisitFrame() {
        // Test for different type, nLocal, local, nStack, and stack values
        // Case 1
        LocalVariablesSorter sorter = new LocalVariablesSorter(1, "", null);
        sorter.visitFrame(1, 5, new Object[]{"int", "float", "long", "double", "java/lang/Object"}, 5, new Object[]{});
        assertEquals(5, sorter.newLocals.length);
        
        // Case 2
        sorter = new LocalVariablesSorter(1, "", null);
        sorter.visitFrame(2, 7, new Object[]{"int", "float", "long", "double", "java/lang/Object", "java/lang/String", "java/lang/Integer"}, 3, new Object[]{});
        assertEquals(7, sorter.newLocals.length);
    }
}