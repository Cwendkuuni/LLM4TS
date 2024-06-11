import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LocalVariablesSorterTest {

    private LocalVariablesSorter localVariablesSorter;

    @Before
    public void setup() {
        localVariablesSorter = new LocalVariablesSorter(1, "()", null);
    }

    @Test
    public void testVisitVarInsn_case22() {
        localVariablesSorter.visitVarInsn(22, 1);
        // add assertions here
    }

    @Test
    public void testVisitVarInsn_case55() {
        localVariablesSorter.visitVarInsn(55, 2);
        // add assertions here
    }

    @Test
    public void testVisitVarInsn_case24() {
        localVariablesSorter.visitVarInsn(24, 3);
        // add assertions here
    }

    @Test
    public void testVisitVarInsn_case57() {
        localVariablesSorter.visitVarInsn(57, 4);
        // add assertions here
    }

    @Test
    public void testVisitVarInsn_case23() {
        localVariablesSorter.visitVarInsn(23, 5);
        // add assertions here
    }

    @Test
    public void testVisitVarInsn_case56() {
        localVariablesSorter.visitVarInsn(56, 6);
        // add assertions here
    }

    @Test
    public void testVisitVarInsn_case21() {
        localVariablesSorter.visitVarInsn(21, 7);
        // add assertions here
    }

    @Test
    public void testVisitVarInsn_case54() {
        localVariablesSorter.visitVarInsn(54, 8);
        // add assertions here
    }

    @Test
    public void testVisitIincInsn() {
        localVariablesSorter.visitIincInsn(9, 10);
        // add assertions here
    }

    @Test
    public void testVisitMaxs() {
        localVariablesSorter.visitMaxs(11, 12);
        // add assertions here
    }

    @Test
    public void testVisitLocalVariable() {
        localVariablesSorter.visitLocalVariable("name", "desc", "signature", null, null, 13);
        // add assertions here
    }

    @Test
    public void testVisitFrame() {
        localVariablesSorter.visitFrame(14, 15, new Object[16], 17, new Object[18]);
        // add assertions here
    }

    @Test
    public void testNewLocal() {
        int result = localVariablesSorter.newLocal(null);
        // add assertions here
    }

    @Test
    public void testSetLocalType() {
        localVariablesSorter.setLocalType(19, null);
        // add assertions here
    }

    @Test
    public void testSetFrameLocal() {
        localVariablesSorter.setFrameLocal(20, null);
        // add assertions here
    }

    @Test
    public void testRemap() {
        int result = localVariablesSorter.remap(21, null);
        // add assertions here
    }

    @Test
    public void testNewLocalMapping() {
        int result = localVariablesSorter.newLocalMapping(null);
        // add assertions here
    }
}