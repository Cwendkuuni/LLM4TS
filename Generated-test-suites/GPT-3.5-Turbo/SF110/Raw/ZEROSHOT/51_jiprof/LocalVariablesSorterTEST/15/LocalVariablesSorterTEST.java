package org.objectweb.asm.jip.commons;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import static org.mockito.Mockito.*;

public class LocalVariablesSorterTest {

    private MethodVisitor mv;
    private LocalVariablesSorter lvs;
    private int i;

    @Before
    public void setUp() {
        mv = mock(MethodVisitor.class);
        lvs = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "Ljava/lang/String;", mv);
        i = 0;
    }

    @Test
    public void testVisitVarInsn() {
        lvs.visitVarInsn(Opcodes.ILOAD, i);
        verify(mv).visitVarInsn(Opcodes.ILOAD, lvs.remap(i, Type.INT_TYPE));

        i++;
        lvs.visitVarInsn(Opcodes.LLOAD, i);
        verify(mv).visitVarInsn(Opcodes.LLOAD, lvs.remap(i, Type.LONG_TYPE));

        i++;
        lvs.visitVarInsn(Opcodes.FLOAD, i);
        verify(mv).visitVarInsn(Opcodes.FLOAD, lvs.remap(i, Type.FLOAT_TYPE));

        i++;
        lvs.visitVarInsn(Opcodes.DLOAD, i);
        verify(mv).visitVarInsn(Opcodes.DLOAD, lvs.remap(i, Type.DOUBLE_TYPE));

        i++;
        lvs.visitVarInsn(Opcodes.ALOAD, i);
        verify(mv).visitVarInsn(Opcodes.ALOAD, lvs.remap(i, LocalVariablesSorter.OBJECT_TYPE));
    }

    @Test
    public void testVisitIincInsn() {
        int var = 1;
        int inc = 2;
        lvs.visitIincInsn(var, inc);
        verify(mv).visitIincInsn(lvs.remap(var, Type.INT_TYPE), inc);
    }

    @Test
    public void testVisitMaxs() {
        int maxStack = 3;
        int maxLocals = 4;
        lvs.visitMaxs(maxStack, maxLocals);
        verify(mv).visitMaxs(maxStack, lvs.nextLocal);
    }

    @Test
    public void testVisitLocalVariable() {
        String name = "var";
        String desc = "Ljava/lang/Object;";
        String signature = "signature";
        org.objectweb.asm.Label start = new org.objectweb.asm.Label();
        org.objectweb.asm.Label end = new org.objectweb.asm.Label();
        int index = 0;
        lvs.visitLocalVariable(name, desc, signature, start, end, index);
        verify(mv).visitLocalVariable(name, desc, signature, start, end, lvs.remap(index, Type.getType(desc)));
    }

    @Test
    public void testVisitFrame() {
        int type = 0;
        int nLocal = 1;
        Object[] local = new Object[2];
        int nStack = 3;
        Object[] stack = new Object[4];
        lvs.visitFrame(type, nLocal, local, nStack, stack);
        if (type != -1) {
            verify(mv).visitFrame(type, nLocal, local, nStack, stack);
        } else if (!lvs.changed()) {
            verify(mv).visitFrame(type, nLocal, local, nStack, stack);
        } else {
            verify(mv).visitFrame(type, nLocal, lvs.newLocals, nStack, stack);
        }
    }

    @Test
    public void testNewLocal() {
        int localIndex = 1;
        Type type = Type.INT_TYPE;
        int nextLocal = lvs.nextLocal;
        lvs.newLocal(type);
        verify(lvs).nextLocal;
        verify(lvs).newLocalMapping(type);
        verify(lvs).setLocalType(localIndex, type);
        verify(lvs).setFrameLocal(localIndex, Opcodes.INTEGER);
    }

    @Test
    public void testSetLocalType() {
        int local = 1;
        Type type = Type.INT_TYPE;
        lvs.setLocalType(local, type);
    }

    @Test
    public void testSetFrameLocal() {
        int local = 1;
        Object type = Opcodes.INTEGER;
        lvs.setFrameLocal(local, type);
    }

    @Test
    public void testRemap() {
        int var = 1;
        Type type = Type.INT_TYPE;

        int firstLocal = lvs.firstLocal;
        int[] mapping = lvs.mapping;

        lvs.remap(var, type);

        verify(lvs).firstLocal;
        verify(lvs).mapping;
        verify(lvs).newLocalMapping(type);
        verify(lvs).setLocalType(lvs.newLocalMapping(type), type);
        verify(lvs).mapping;

        if (firstLocal > var) {
            verify(lvs, never()).newLocalMapping(type);
            verify(lvs, never()).setLocalType(anyInt(), any());
        } else {
            verify(lvs).newLocalMapping(type);
            verify(lvs).setLocalType(anyInt(), any());
        }
    }

    @Test
    public void testNewLocalMapping() {
        Type type = Type.INT_TYPE;
        int local = lvs.nextLocal;
        int nextLocal = lvs.nextLocal + type.getSize();
        lvs.newLocalMapping(type);
        verify(lvs).nextLocal;
    }
}