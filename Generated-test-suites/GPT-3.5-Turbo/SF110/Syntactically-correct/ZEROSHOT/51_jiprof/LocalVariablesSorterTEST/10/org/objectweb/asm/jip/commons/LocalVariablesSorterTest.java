import org.junit.*;
import org.objectweb.asm.*;
import org.objectweb.asm.jip.commons.*;

public class LocalVariablesSorterTest {
    
    @Test
    public void testVisitVarInsn() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "", null);
        sorter.visitVarInsn(22, 1);
        sorter.visitVarInsn(24, 2);
        sorter.visitVarInsn(23, 3);
        sorter.visitVarInsn(21, 4);
        sorter.visitVarInsn(25, 5);
    }
    
    @Test
    public void testVisitIincInsn() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "", null);
        sorter.visitIincInsn(1, 2);
        sorter.visitIincInsn(3, 4);
        sorter.visitIincInsn(5, -1);
    }
    
    @Test
    public void testVisitMaxs() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "", null);
        sorter.visitMaxs(10, 20);
    }
    
    @Test
    public void testVisitLocalVariable() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "", null);
        sorter.visitLocalVariable("name1", "desc1", "signature1", new Label(), new Label(), 1);
        sorter.visitLocalVariable("name2", "desc2", "signature2", new Label(), new Label(), 2);
        sorter.visitLocalVariable("name3", "desc3", "signature3", new Label(), new Label(), 3);
    }
    
    @Test
    public void testVisitFrame() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "", null);
        sorter.visitFrame(-1, 10, new Object[10], 5, new Object[5]);
    }
    
    @Test
    public void testNewLocal() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "", null);
        sorter.newLocal(Type.INT_TYPE);
        sorter.newLocal(Type.FLOAT_TYPE);
        sorter.newLocal(Type.LONG_TYPE);
        sorter.newLocal(Type.DOUBLE_TYPE);
        sorter.newLocal(Type.getType("Ljava/lang/String;"));
    }
    
    @Test
    public void testSetLocalType() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "", null);
        sorter.setLocalType(1, Type.INT_TYPE);
        sorter.setLocalType(2, Type.FLOAT_TYPE);
        sorter.setLocalType(3, Type.LONG_TYPE);
        sorter.setLocalType(4, Type.DOUBLE_TYPE);
    }
    
    @Test
    public void testSetFrameLocal() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "", null);
        sorter.setFrameLocal(1, Opcodes.INTEGER);
        sorter.setFrameLocal(2, Opcodes.FLOAT);
        sorter.setFrameLocal(3, Opcodes.LONG);
        sorter.setFrameLocal(4, Opcodes.DOUBLE);
    }
    
    @Test
    public void testRemap() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "", null);
        sorter.remap(1, Type.INT_TYPE);
        sorter.remap(2, Type.FLOAT_TYPE);
        sorter.remap(3, Type.LONG_TYPE);
        sorter.remap(4, Type.DOUBLE_TYPE);
    }
    
    @Test
    public void testNewLocalMapping() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "", null);
        sorter.newLocalMapping(Type.INT_TYPE);
        sorter.newLocalMapping(Type.FLOAT_TYPE);
        sorter.newLocalMapping(Type.LONG_TYPE);
        sorter.newLocalMapping(Type.DOUBLE_TYPE);
    }
}