package org.objectweb.asm.jip.commons;

import org.junit.*;
import static org.junit.Assert.*;

public class LocalVariablesSorterTest {
    
    @Test
    public void testVisitVarInsn() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(1, "VL", null);
        sorter.visitVarInsn(21, 1);
        // assert statements
    }
    
    @Test
    public void testVisitIincInsn() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(1, "VL", null);
        sorter.visitIincInsn(1, 1);
        // assert statements
    }
    
    @Test
    public void testVisitMaxs() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(1, "VL", null);
        sorter.visitMaxs(2, 2);
        // assert statements
    }
    
    @Test
    public void testVisitLocalVariable() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(1, "VL", null);
        sorter.visitLocalVariable("name", "desc", "signature", null, null, 1);
        // assert statements
    }
    
    @Test
    public void testVisitFrame() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(1, "VL", null);
        sorter.visitFrame(1, 1, new Object[1], 1, new Object[1]);
        // assert statements
    }
    
    @Test
    public void testNewLocal() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(1, "VL", null);
        sorter.newLocal(Type.INT_TYPE);
        // assert statements
    }
    
    @Test
    public void testSetLocalType() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(1, "VL", null);
        sorter.setLocalType(1, Type.INT_TYPE);
        // assert statements
    }
    
    @Test
    public void testSetFrameLocal() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(1, "VL", null);
        sorter.setFrameLocal(1, Opcodes.INTEGER);
        // assert statements
    }
    
    @Test
    public void testRemap() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(1, "VL", null);
        sorter.remap(1, Type.INT_TYPE);
        // assert statements
    }
    
    @Test
    public void testNewLocalMapping() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(1, "VL", null);
        sorter.newLocalMapping(Type.INT_TYPE);
        // assert statements
    }
}