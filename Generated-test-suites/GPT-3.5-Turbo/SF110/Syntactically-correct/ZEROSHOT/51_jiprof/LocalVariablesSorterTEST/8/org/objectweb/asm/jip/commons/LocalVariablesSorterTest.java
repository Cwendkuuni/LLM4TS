package org.objectweb.asm.jip.commons;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.Type;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Label;

public class LocalVariablesSorterTest {
    private MethodVisitor mv;
    private LocalVariablesSorter lvs;
    
    @Before
    public void setUp() {
        mv = new MethodVisitor() {
            public void visitVarInsn(int opcode, int var) {}
            public void visitIincInsn(int var, int increment) {}
            public void visitMaxs(int maxStack, int maxLocals) {}
            public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {}
            public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {}
        };
        lvs = new LocalVariablesSorter(0, "desc", mv);
    }
    
    @Test
    public void testLocalVariablesSorter() {
        assertEquals(40, lvs.mapping.length);
        assertEquals(20, lvs.newLocals.length);
        assertEquals(0, lvs.nextLocal);
        assertEquals(1, lvs.firstLocal);
        assertFalse(lvs.changed);
    }
    
    @Test
    public void testVisitVarInsn() {
        lvs.visitVarInsn(21, 1);
        // assert visitVarInsn implementation
    }
    
    @Test
    public void testVisitIincInsn() {
        lvs.visitIincInsn(1, 1);
        // assert visitIincInsn implementation
    }
    
    @Test
    public void testVisitMaxs() {
        lvs.visitMaxs(1, 1);
        // assert visitMaxs implementation
    }
    
    @Test
    public void testVisitLocalVariable() {
        lvs.visitLocalVariable("name", "desc", "signature", new Label(), new Label(), 1);
        // assert visitLocalVariable implementation
    }
    
    @Test
    public void testVisitFrame() {
        lvs.visitFrame(1, 1, new Object[1], 1, new Object[1]);
        // assert visitFrame implementation
    }
    
    @Test
    public void testNewLocal() {
        Type type = Type.INT_TYPE;
        int local = lvs.newLocal(type);
        // assert newLocal implementation
    }
    
    @Test
    public void testSetLocalType() {
        lvs.setLocalType(1, Type.INT_TYPE);
        // assert setLocalType implementation
    }
    
    @Test
    public void testSetFrameLocal() {
        lvs.setFrameLocal(1, Opcodes.TOP);
        // assert setFrameLocal implementation
    }
    
    @Test
    public void testRemap() {
        int var = 1;
        Type type = Type.INT_TYPE;
        int result = lvs.remap(var, type);
        // assert remap implementation
    }
    
    @Test
    public void testNewLocalMapping() {
        Type type = Type.INT_TYPE;
        int result = lvs.newLocalMapping(type);
        // assert newLocalMapping implementation
    }
}