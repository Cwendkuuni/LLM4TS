import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TypeParserTest {

    private TypeParser parser;

    @Before
    public void setUp() {
        parser = new TypeParser(new TypeFactory());
    }

    @Test
    public void testParse_SingleGenericType_ReturnsJavaType() {
        String canonical = "com.example.GenericClass<String>";
        JavaType result = parser.parse(canonical);
        Assert.assertNotNull(result);
        Assert.assertTrue(result.isTypeOrSubTypeOf(GenericClass.class));
        Assert.assertEquals(String.class, result.containedType(0).getRawClass());
    }

    @Test
    public void testParse_MultipleGenericType_ReturnsJavaType() {
        String canonical = "com.example.GenericClass<String,Integer>";
        JavaType result = parser.parse(canonical);
        Assert.assertNotNull(result);
        Assert.assertTrue(result.isTypeOrSubTypeOf(GenericClass.class));
        Assert.assertEquals(String.class, result.containedType(0).getRawClass());
        Assert.assertEquals(Integer.class, result.containedType(1).getRawClass());
    }

    @Test
    public void testParse_NonGenericType_ReturnsJavaType() {
        String canonical = "com.example.SimpleClass";
        JavaType result = parser.parse(canonical);
        Assert.assertNotNull(result);
        Assert.assertEquals(SimpleClass.class, result.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_InvalidCanonical_ThrowsIllegalArgumentException() {
        String canonical = "invalidCanonical";
        parser.parse(canonical);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_UnexpectedTokensAfterCompleteType_ThrowsIllegalArgumentException() {
        String canonical = "com.example.SimpleClass extraToken";
        parser.parse(canonical);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_UnexpectedEndOfString_ThrowsIllegalArgumentException() {
        String canonical = "";
        parser.parse(canonical);
    }
}