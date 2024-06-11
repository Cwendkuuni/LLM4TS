package org.objectweb.asm.jip.commons;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.TypeInfo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class LocalVariablesSorterTest {

    private LocalVariablesSorter localVariablesSorter;

    @Before
    public void setup() {
        MethodVisitor methodVisitor = new MethodVisitor() {
            @Override
            public void visitVarInsn(int opcode, int var) {
                // test visitVarInsn
            }

            @Override
            public void visitIincInsn(int var, int increment) {
                // test visitIincInsn
            }

            @Override
            public void visitMaxs(int maxStack, int maxLocals) {
                // test visitMaxs
            }

            @Override
            public void visitLocalVariable(String name, String desc, String signature, org.objectweb.asm.Label start,
                                           org.objectweb.asm.Label end, int index) {
                // test visitLocalVariable
            }

            @Override
            public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
                // test visitFrame
            }
        };

        localVariablesSorter = new LocalVariablesSorter(1, "desc", methodVisitor);
    }

    @Test
    public void testVisitVarInsn() {
        // write your test cases for visitVarInsn method here
    }

    @Test
    public void testVisitIincInsn() {
        // write your test cases for visitIincInsn method here
    }

    @Test
    public void testVisitMaxs() {
        // write your test cases for visitMaxs method here
    }

    @Test
    public void testVisitLocalVariable() {
        // write your test cases for visitLocalVariable method here
    }

    @Test
    public void testVisitFrame() {
        // write your test cases for visitFrame method here
    }

    @Test
    public void testNewLocal() {
        // write your test cases for newLocal method here
    }

    @Test
    public void testSetLocalType() {
        // write your test cases for setLocalType method here
    }

    @Test
    public void testSetFrameLocal() {
        // write your test cases for setFrameLocal method here
    }

    @Test
    public void testRemap() {
        // write your test cases for remap method here
    }

    @Test
    public void testNewLocalMapping() {
        // write your test cases for newLocalMapping method here
    }

}