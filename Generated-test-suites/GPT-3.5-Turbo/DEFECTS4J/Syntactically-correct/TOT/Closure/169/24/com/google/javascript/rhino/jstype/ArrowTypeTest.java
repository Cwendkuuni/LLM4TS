import org.junit.Assert;
import org.junit.Test;

public class ArrowTypeTest {

    private JSTypeRegistry registry;

    public ArrowTypeTest() {
        registry = new JSTypeRegistry();
    }

    @Test
    public void testIsSubtype_Covariant_ReturnType() {
        ArrowType arrowType1 = new ArrowType(registry, null, registry.getNativeType("string"));
        ArrowType arrowType2 = new ArrowType(registry, null, registry.getNativeType("object"));
        boolean result = arrowType1.isSubtype(arrowType2);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsSubtype_Contravariant_ParameterTypes() {
        ArrowType arrowType1 = new ArrowType(registry, registry.createParametersWithVarArgs(registry.getNativeType("string")), registry.getNativeType("object"));
        ArrowType arrowType2 = new ArrowType(registry, registry.createParametersWithVarArgs(registry.getNativeType("object")), registry.getNativeType("string"));
        boolean result = arrowType1.isSubtype(arrowType2);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsSubtype_DespiteDifferentParameterLists() {
        ArrowType arrowType1 = new ArrowType(registry, registry.createParametersWithVarArgs(registry.getNativeType("string")), registry.getNativeType("object"));
        ArrowType arrowType2 = new ArrowType(registry, registry.createParametersWithVarArgs(registry.getNativeType("string"), registry.getNativeType("number")), registry.getNativeType("object"));
        boolean result = arrowType1.isSubtype(arrowType2);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsSubtype_RequiredArgumentMissing() {
        ArrowType arrowType1 = new ArrowType(registry, registry.createParametersWithVarArgs(registry.getNativeType("string"), registry.getNativeType("number")), registry.getNativeType("object"));
        ArrowType arrowType2 = new ArrowType(registry, registry.createParametersWithVarArgs(registry.getNativeType("string")), registry.getNativeType("object"));
        boolean result = arrowType1.isSubtype(arrowType2);
        Assert.assertFalse(result);
    }

    @Test
    public void testHasEqualParameters_ParameterListsAreEqual() {
        ArrowType arrowType1 = new ArrowType(registry, registry.createParametersWithVarArgs(registry.getNativeType("string"), registry.getNativeType("number")), registry.getNativeType("object"));
        ArrowType arrowType2 = new ArrowType(registry, registry.createParametersWithVarArgs(registry.getNativeType("string"), registry.getNativeType("number")), registry.getNativeType("string"));
        boolean result = arrowType1.hasEqualParameters(arrowType2, false);
        Assert.assertTrue(result);
    }

    @Test
    public void testCheckArrowEquivalenceHelper_EquivalentTypes() {
        ArrowType arrowType1 = new ArrowType(registry, registry.createParametersWithVarArgs(registry.getNativeType("string"), registry.getNativeType("number")), registry.getNativeType("object"));
        ArrowType arrowType2 = new ArrowType(registry, registry.createParametersWithVarArgs(registry.getNativeType("string"), registry.getNativeType("number")), registry.getNativeType("object"));
        boolean result = arrowType1.checkArrowEquivalenceHelper(arrowType2, false);
        Assert.assertTrue(result);
    }

    @Test
    public void testGetLeastSupertype_UnsupportedOperationException() {
        ArrowType arrowType = new ArrowType(registry, registry.createParametersWithVarArgs(registry.getNativeType("string"), registry.getNativeType("number")), registry.getNativeType("object"));
        try {
            arrowType.getLeastSupertype(registry.getNativeType("object"));
            Assert.fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            Assert.assertEquals("Method not supported", e.getMessage());
        }
    }

    @Test
    public void testGetGreatestSubtype_UnsupportedOperationException() {
        ArrowType arrowType = new ArrowType(registry, registry.createParametersWithVarArgs(registry.getNativeType("string"), registry.getNativeType("number")), registry.getNativeType("object"));
        try {
            arrowType.getGreatestSubtype(registry.getNativeType("object"));
            Assert.fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            Assert.assertEquals("Method not supported", e.getMessage());
        }
    }

    @Test
    public void testTestForEquality_UnsupportedOperationException() {
        ArrowType arrowType = new ArrowType(registry, registry.createParametersWithVarArgs(registry.getNativeType("string"), registry.getNativeType("number")), registry.getNativeType("object"));
        try {
            arrowType.testForEquality(registry.getNativeType("object"));
            Assert.fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            Assert.assertEquals("Method not supported", e.getMessage());
        }
    }

    @Test
    public void testVisit_UnsupportedOperationException() {
        ArrowType arrowType = new ArrowType(registry, registry.createParametersWithVarArgs(registry.getNativeType("string"), registry.getNativeType("number")), registry.getNativeType("object"));
        try {
            arrowType.visit(new Visitor<String>() {});
            Assert.fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            Assert.assertEquals("Method not supported", e.getMessage());
        }
    }

    @Test
    public void testGetPossibleToBooleanOutcomes() {
        ArrowType arrowType = new ArrowType(registry, registry.createParametersWithVarArgs(registry.getNativeType("string"), registry.getNativeType("number")), registry.getNativeType("object"));
        BooleanLiteralSet result = arrowType.getPossibleToBooleanOutcomes();
        Assert.assertEquals(BooleanLiteralSet.TRUE, result);
    }

    @Test
    public void testResolveInternal_UnknownParamsOrReturn() {
        ArrowType arrowType = new ArrowType(registry, registry.createParametersWithVarArgs(registry.getNativeType("string"), registry.getNativeType("number")), registry.getNativeType("unknown"));
        boolean result = arrowType.hasUnknownParamsOrReturn();
        Assert.assertTrue(result);
    }

    @Test
    public void testHasAnyTemplateInternal() {
        ArrowType arrowType = new ArrowType(registry, registry.createParametersWithVarArgs(registry.getNativeType("string"), registry.getNativeType("number")), registry.getNativeType("object"));
        boolean result = arrowType.hasAnyTemplateInternal();
        Assert.assertFalse(result);
    }
}