import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.jip.MethodVisitor;
import org.objectweb.asm.jip.commons.LocalVariablesSorter;

public class LocalVariablesSorterTest {
    @Test
    public void testVisitVarInsn() {
        MethodVisitor mv = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", null);
        mv.visitVarInsn(Opcodes.ILOAD, 1);
        mv.visitVarInsn(Opcodes.LLOAD, 2);
        mv.visitVarInsn(Opcodes.FLOAD, 3);
        mv.visitVarInsn(Opcodes.DLOAD, 4);
        mv.visitVarInsn(Opcodes.ALOAD, 5);
        mv.visitVarInsn(Opcodes.ISTORE, 6);
        mv.visitVarInsn(Opcodes.LSTORE, 7);
        mv.visitVarInsn(Opcodes.FSTORE, 8);
        mv.visitVarInsn(Opcodes.DSTORE, 9);
        mv.visitVarInsn(Opcodes.ASTORE, 10);

        // TODO: Add assertions

    }

    @Test
    public void testVisitIincInsn() {
        MethodVisitor mv = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", null);
        mv.visitIincInsn(1, 1);
        mv.visitIincInsn(2, 2);

        // TODO: Add assertions

    }

    @Test
    public void testVisitMaxs() {
        MethodVisitor mv = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", null);
        mv.visitMaxs(1, 2);

        // TODO: Add assertions

    }

    @Test
    public void testVisitLocalVariable() {
        MethodVisitor mv = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", null);
        mv.visitLocalVariable("name", "desc", "signature", null, null, 1);

        // TODO: Add assertions

    }

    @Test
    public void testVisitFrame() {
        MethodVisitor mv = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", null);
        mv.visitFrame(Opcodes.F_NEW, 1, new Object[]{"object"}, 2, new Object[]{"object1", "object2"});

        // TODO: Add assertions

    }

    @Test
    public void testNewLocal() {
        MethodVisitor mv = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", null);
        int local1 = ((LocalVariablesSorter) mv).newLocal(Type.INT_TYPE);
        int local2 = ((LocalVariablesSorter) mv).newLocal(Type.FLOAT_TYPE);
        int local3 = ((LocalVariablesSorter) mv).newLocal(Type.LONG_TYPE);
        int local4 = ((LocalVariablesSorter) mv).newLocal(Type.DOUBLE_TYPE);
        int local5 = ((LocalVariablesSorter) mv).newLocal(Type.getObjectType("java/lang/Object"));

        // TODO: Add assertions

    }

    @Test
    public void testSetLocalType() {
        MethodVisitor mv = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", null);
        ((LocalVariablesSorter) mv).setLocalType(1, Type.INT_TYPE);

        // TODO: Add assertions

    }

    @Test
    public void testSetFrameLocal() {
        MethodVisitor mv = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", null);
        ((LocalVariablesSorter) mv).setFrameLocal(1, Opcodes.INTEGER);

        // TODO: Add assertions

    }

    @Test
    public void testRemap() {
        MethodVisitor mv = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", null);
        int remapped1 = ((LocalVariablesSorter) mv).remap(1, Type.INT_TYPE);
        int remapped2 = ((LocalVariablesSorter) mv).remap(2, Type.FLOAT_TYPE);
        int remapped3 = ((LocalVariablesSorter) mv).remap(3, Type.LONG_TYPE);
        int remapped4 = ((LocalVariablesSorter) mv).remap(4, Type.DOUBLE_TYPE);
        int remapped5 = ((LocalVariablesSorter) mv).remap(5, Type.getObjectType("java/lang/Object"));

        // TODO: Add assertions

    }

    @Test
    public void testNewLocalMapping() {
        MethodVisitor mv = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", null);
        int mapping1 = ((LocalVariablesSorter) mv).newLocalMapping(Type.INT_TYPE);
        int mapping2 = ((LocalVariablesSorter) mv).newLocalMapping(Type.FLOAT_TYPE);
        int mapping3 = ((LocalVariablesSorter) mv).newLocalMapping(Type.LONG_TYPE);
        int mapping4 = ((LocalVariablesSorter) mv).newLocalMapping(Type.DOUBLE_TYPE);
        int mapping5 = ((LocalVariablesSorter) mv).newLocalMapping(Type.getObjectType("java/lang/Object"));

        // TODO: Add assertions

    }
}