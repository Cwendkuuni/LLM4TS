package org.objectweb.asm.jip.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

public class LocalVariablesSorterTest {
    private LocalVariablesSorter sorter;
    private MethodVisitor mvMock;
    
    @Before
    public void setup() {
        mvMock = mock(MethodVisitor.class);
        sorter = new LocalVariablesSorter(0, "()V", mvMock);
    }

    @Test
    public void testVisitVarInsn() {
        sorter.visitInsn(22, 1);
        sorter.visitInsn(55, 2);
        sorter.visitInsn(24, 3);
        sorter.visitInsn(57, 4);
        sorter.visitInsn(23, 5);
        sorter.visitInsn(56, 6);
        sorter.visitInsn(21, 7);
        sorter.visitInsn(54, 8);
        sorter.visitInsn(0, 9);

        assertEquals(1, sorter.remap(1, Type.LONG_TYPE));
        assertEquals(2, sorter.remap(2, Type.LONG_TYPE));
        assertEquals(3, sorter.remap(3, Type.DOUBLE_TYPE));
        assertEquals(4, sorter.remap(4, Type.DOUBLE_TYPE));
        assertEquals(5, sorter.remap(5, Type.FLOAT_TYPE));
        assertEquals(6, sorter.remap(6, Type.FLOAT_TYPE));
        assertEquals(7, sorter.remap(7, Type.INT_TYPE));
        assertEquals(8, sorter.remap(8, Type.INT_TYPE));
        assertEquals(9, sorter.remap(9, Type.getObjectType("java/lang/Object")));
    }

    @Test
    public void testVisitIincInsn() {
        sorter.visitIincInsn(1, 10);
        sorter.visitIincInsn(2, 20);
        sorter.visitIincInsn(3, 30);

        assertEquals(1, sorter.remap(1, Type.INT_TYPE));
        assertEquals(2, sorter.remap(2, Type.INT_TYPE));
        assertEquals(3, sorter.remap(3, Type.INT_TYPE));
    }

    @Test
    public void testVisitMaxs() {
        sorter.visitMaxs(5, 10);
        assertEquals(10, sorter.nextLocal);
    }

    @Test
    public void testVisitLocalVariable() {
        Label start = new Label();
        Label end = new Label();
        sorter.visitLocalVariable("name", "desc", "signature", start, end, 1);

        assertEquals(1, sorter.remap(1, Type.getType("desc")));
        assertEquals(1, sorter.newLocalMapping(Type.getType("desc")));
    }

    @Test(expected = IllegalStateException.class)
    public void testVisitFrame() {
        sorter.visitFrame(-1, 2, new Object[]{Opcodes.INTEGER, Opcodes.FLOAT}, 1, new Object[]{Opcodes.INTEGER});
        sorter.visitFrame(1, 2, new Object[]{Opcodes.INTEGER, Opcodes.FLOAT}, 1, new Object[]{Opcodes.INTEGER});
        sorter.changed = true;
        sorter.visitFrame(-1, 2, new Object[]{Opcodes.INTEGER, Opcodes.FLOAT}, 1, new Object[]{Opcodes.INTEGER});
    }

    @Test
    public void testNewLocal() {
        assertEquals(1, sorter.newLocal(Type.INT_TYPE));
        assertEquals(2, sorter.newLocal(Type.FLOAT_TYPE));
        assertEquals(3, sorter.newLocal(Type.LONG_TYPE));
        assertEquals(4, sorter.newLocal(Type.DOUBLE_TYPE));
        assertEquals(5, sorter.newLocal(Type.getObjectType("desc")));
        assertEquals(6, sorter.nextLocal);
    }
}