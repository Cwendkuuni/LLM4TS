
import org.junit.Test;
import org.objectweb.asm.jip.MethodVisitor;
import org.objectweb.asm.jip.Type;
import org.objectweb.asm.jip.commons.LocalVariablesSorter;

public class LocalVariablesSorterTest {

    @Test
    public void testVarInsn() {
        // Test visitVarInsn method
        LocalVariablesSorter lvs = new LocalVariablesSorter(1, "I", new MethodVisitor() {
            @Override
            public void visitVarInsn(int opcode, int var) {
                assert(opcode >= 21 && opcode <= 25) : "Invalid opcode";
                assert(var >= 0) : "Invalid var";
            }
        });
        
        lvs.visitVarInsn(Type.INT_TYPE.getOpcode(21), 0);
        lvs.visitVarInsn(Type.LONG_TYPE.getOpcode(22), 1);
        lvs.visitVarInsn(Type.FLOAT_TYPE.getOpcode(23), 2);
        lvs.visitVarInsn(Type.DOUBLE_TYPE.getOpcode(24), 3);
        lvs.visitVarInsn(Type.OBJECT_TYPE.getOpcode(25), 4);
    }

    @Test
    public void testVisitIincInsn() {
        // Test visitIincInsn method
        LocalVariablesSorter lvs = new LocalVariablesSorter(1, "I", new MethodVisitor() {
            @Override
            public void visitIincInsn(int var, int increment) {
                assert(var >= 0) : "Invalid var";
                assert(increment != 0) : "Invalid increment";
            }
        });
        
        lvs.visitIincInsn(0, 1);
        lvs.visitIincInsn(1, -1);
    }

    @Test
    public void testVisitMaxs() {
        // Test visitMaxs method
        LocalVariablesSorter lvs = new LocalVariablesSorter(1, "I", new MethodVisitor() {
            @Override
            public void visitMaxs(int maxStack, int maxLocals) {
                assert(maxStack >= 0): "Invalid maxStack";
                assert(maxLocals >= 0): "Invalid maxLocals";
            }
        });
        
        lvs.visitMaxs(10, 5);
    }

    @Test
    public void testVisitLocalVariable() {
        // Test visitLocalVariable method
        LocalVariablesSorter lvs = new LocalVariablesSorter(1, "I", new MethodVisitor() {
            @Override
            public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
                assert(name != null): "Invalid name";
                assert(desc != null): "Invalid desc";
                assert(signature != null): "Invalid signature";
                assert(index >= 0): "Invalid index";
            }
        });
        
        lvs.visitLocalVariable("var1", "I", null, null, null, 0);
        lvs.visitLocalVariable("var2", "Ljava/lang/Object;", null, null, null, 1);
    }

    @Test
    public void testVisitFrame() {
        // Test visitFrame method
        LocalVariablesSorter lvs = new LocalVariablesSorter(1, "I", new MethodVisitor() {
            @Override
            public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
                assert(type == -1) : "Invalid type";
                assert(nLocal >= 0) : "Invalid nLocal";
                assert(local != null) : "Invalid local";
                assert(nStack >= 0) : "Invalid nStack";
                assert(stack != null) : "Invalid stack";
            }
        });
        
        lvs.visitFrame(-1, 3, new Object[]{Type.INT_TYPE, Type.INT_TYPE, Type.INT_TYPE}, 2, new Object[]{Type.OBJECT_TYPE, Type.OBJECT_TYPE});
    }
    
    @Test
    public void testNewLocalMapping() {
        // Test newLocalMapping method
        LocalVariablesSorter lvs = new LocalVariablesSorter(1, "I", new MethodVisitor() {});
        
        int local1 = lvs.newLocalMapping(Type.INT_TYPE);
        int local2 = lvs.newLocalMapping(Type.LONG_TYPE);
        int local3 = lvs.newLocalMapping(Type.OBJECT_TYPE);
        
        assert(local1 == 1) : "Invalid local1";
        assert(local2 == 2) : "Invalid local2";
        assert(local3 == 4) : "Invalid local3";
    }
}
