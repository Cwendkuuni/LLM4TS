import org.junit.Assert;
import org.junit.Test;
import org.objectweb.asm.jip.LocalVariablesSorter;

public class LocalVariablesSorterTest {

    @Test
    public void testVisitVarInsn_code22_var0() {
        // Arrange
        int expectedOpcode = 22;
        int expectedVar = 0;
        LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(0, "desc", null);
        
        // Act
        localVariablesSorter.visitVarInsn(expectedOpcode, expectedVar);

        // Assert
        int actualOpcode = localVariablesSorter.getVisitedOpcode();
        int actualVar = localVariablesSorter.getVisitedVar();
        Assert.assertEquals(expectedOpcode, actualOpcode);
        Assert.assertEquals(expectedVar, actualVar);
    }
    
    @Test
    public void testVisitIincInsn_var1_increment10() {
        // Arrange
        int expectedVar = 1;
        int expectedIncrement = 10;
        LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(0, "desc", null);
        
        // Act
        localVariablesSorter.visitIincInsn(expectedVar, expectedIncrement);

        // Assert
        int actualVar = localVariablesSorter.getVisitedVar();
        int actualIncrement = localVariablesSorter.getVisitedIncrement();
        Assert.assertEquals(expectedVar, actualVar);
        Assert.assertEquals(expectedIncrement, actualIncrement);
    }
    
    @Test
    public void testVisitMaxs_maxStack10_maxLocals20() {
        // Arrange
        int expectedMaxStack = 10;
        int expectedMaxLocals = 20;
        LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(0, "desc", null);
        
        // Act
        localVariablesSorter.visitMaxs(expectedMaxStack, expectedMaxLocals);

        // Assert
        int actualMaxStack = localVariablesSorter.getVisitedMaxStack();
        int actualMaxLocals = localVariablesSorter.getVisitedMaxLocals();
        Assert.assertEquals(expectedMaxStack, actualMaxStack);
        Assert.assertEquals(expectedMaxLocals, actualMaxLocals);
    }
    
    @Test
    public void testVisitLocalVariable_nameVar1_descString_signatureLabelStartLabelEndIndex0() {
        // Arrange
        String expectedName = "var1";
        String expectedDesc = "String";
        String expectedSignature = "signature";
        Label expectedStart = new Label();
        Label expectedEnd = new Label();
        int expectedIndex = 0;
        LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(0, "desc", null);
        
        // Act
        localVariablesSorter.visitLocalVariable(expectedName, expectedDesc, expectedSignature, expectedStart, expectedEnd, expectedIndex);

        // Assert
        String actualName = localVariablesSorter.getVisitedName();
        String actualDesc = localVariablesSorter.getVisitedDesc();
        String actualSignature = localVariablesSorter.getVisitedSignature();
        Label actualStart = localVariablesSorter.getVisitedStart();
        Label actualEnd = localVariablesSorter.getVisitedEnd();
        int actualIndex = localVariablesSorter.getVisitedIndex();
        Assert.assertEquals(expectedName, actualName);
        Assert.assertEquals(expectedDesc, actualDesc);
        Assert.assertEquals(expectedSignature, actualSignature);
        Assert.assertEquals(expectedStart, actualStart);
        Assert.assertEquals(expectedEnd, actualEnd);
        Assert.assertEquals(expectedIndex, actualIndex);
    }
    
    @Test
    public void testVisitFrame_typeNegative_nLocal10_localObjectsArray_nStack5_stackObjectsArray() {
        // Arrange
        int expectedType = -1;
        int expectedNLocal = 10;
        Object[] expectedLocalObjects = new Object[expectedNLocal];
        int expectedNStack = 5;
        Object[] expectedStackObjects = new Object[expectedNStack];
        LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(0, "desc", null);
        
        // Act
        localVariablesSorter.visitFrame(expectedType, expectedNLocal, expectedLocalObjects, expectedNStack, expectedStackObjects);

        // Assert
        int actualType = localVariablesSorter.getVisitedFrameType();
        int actualNLocal = localVariablesSorter.getVisitedFrameNLocal();
        Object[] actualLocalObjects = localVariablesSorter.getVisitedFrameLocal();
        int actualNStack = localVariablesSorter.getVisitedFrameNStack();
        Object[] actualStackObjects = localVariablesSorter.getVisitedFrameStack();
        Assert.assertEquals(expectedType, actualType);
        Assert.assertEquals(expectedNLocal, actualNLocal);
        Assert.assertArrayEquals(expectedLocalObjects, actualLocalObjects);
        Assert.assertEquals(expectedNStack, actualNStack);
        Assert.assertArrayEquals(expectedStackObjects, actualStackObjects);
    }
    
    @Test
    public void testNewLocal_typeInteger() {
        // Arrange
        String expectedTypeDescriptor = "Ljava/lang/Integer;";
        LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(0, "desc", null);
        
        // Act
        int actualLocal = localVariablesSorter.newLocal(Type.getType(expectedTypeDescriptor));

        // Assert
        String actualTypeDescriptor = localVariablesSorter.getVisitedTypeDescriptor();
        Assert.assertEquals(expectedTypeDescriptor, actualTypeDescriptor);
        Assert.assertTrue(actualLocal >= localVariablesSorter.getFirstLocal());
    }
    
    @Test
    public void testSetLocalType_local1_typeString() {
        // Arrange
        int expectedLocal = 1;
        String expectedTypeDescriptor = "Ljava/lang/String;";
        LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(0, "desc", null);
        
        // Act
        localVariablesSorter.setLocalType(expectedLocal, Type.getType(expectedTypeDescriptor));

        // Assert
        int actualLocal = localVariablesSorter.getVisitedLocal();
        String actualTypeDescriptor = localVariablesSorter.getVisitedTypeDescriptor();
        Assert.assertEquals(expectedLocal, actualLocal);
        Assert.assertEquals(expectedTypeDescriptor, actualTypeDescriptor);
    }
    
    @Test
    public void testSetFrameLocal_local1_typeInteger() {
        // Arrange
        int expectedLocal = 1;
        int expectedDataType = Opcodes.INTEGER;
        LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(0, "desc", null);
        
        // Act
        localVariablesSorter.setFrameLocal(expectedLocal, expectedDataType);

        // Assert
        int actualLocal = localVariablesSorter.getVisitedLocal();
        int actualDataType = (int)localVariablesSorter.getVisitedDataType();
        Assert.assertEquals(expectedLocal, actualLocal);
        Assert.assertEquals(expectedDataType, actualDataType);
    }
    
    @Test
    public void testRemap_var5_typeLong() {
        // Arrange
        int expectedVar = 5;
        int expectedDataType = Opcodes.LONG;
        LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(0, "desc", null);
        
        // Act
        int actualVar = localVariablesSorter.remap(expectedVar, Type.getType(expectedDataType));

        // Assert
        int actualDataType = (int)localVariablesSorter.getVisitedDataType();
        Assert.assertEquals(expectedVar, actualVar);
        Assert.assertEquals(expectedDataType, actualDataType);
    }
    
    @Test
    public void testNewLocalMapping_typeObject() {
        // Arrange
        String expectedTypeDescriptor = "Ljava/lang/Object;";
        LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(0, "desc", null);
        
        // Act
        int actualLocal = localVariablesSorter.newLocalMapping(Type.getType(expectedTypeDescriptor));

        // Assert
        String actualTypeDescriptor = localVariablesSorter.getVisitedTypeDescriptor();
        Assert.assertEquals(expectedTypeDescriptor, actualTypeDescriptor);
        Assert.assertTrue(actualLocal >= localVariablesSorter.getFirstLocal());
    }
}