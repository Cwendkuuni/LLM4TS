import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class TypeParserTest {
    private TypeParser typeParser;

    @Before
    public void setUp() throws Exception {
        TypeFactory typeFactory = new TypeFactory();
        typeParser = new TypeParser(typeFactory);
    }

    @Test
    public void testParse() {
        try {
            JavaType result = typeParser.parse("java.util.List");
            Assert.assertNotNull(result);
            Assert.assertEquals("java.util.List", result.getRawClass().getName());
        } catch (IllegalArgumentException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testParseType() {
        TypeParser.MyTokenizer tokenizer = typeParser.new MyTokenizer("java.util.Map<java.lang.String, java.lang.Integer>");
        try {
            JavaType result = typeParser.parseType(tokenizer);
            Assert.assertNotNull(result);
            Assert.assertEquals("java.util.Map", result.getRawClass().getName());

            List<JavaType> typeParameters = result.getTypeParameters();
            Assert.assertEquals(2, typeParameters.size());
            Assert.assertEquals("java.lang.String", typeParameters.get(0).getRawClass().getName());
            Assert.assertEquals("java.lang.Integer", typeParameters.get(1).getRawClass().getName());
        } catch (IllegalArgumentException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testParseTypes() {
        TypeParser.MyTokenizer tokenizer = typeParser.new MyTokenizer("java.util.List<java.lang.String, java.lang.Integer>");
        try {
            List<JavaType> result = typeParser.parseTypes(tokenizer);
            Assert.assertNotNull(result);
            Assert.assertEquals(2, result.size());
            Assert.assertEquals("java.lang.String", result.get(0).getRawClass().getName());
            Assert.assertEquals("java.lang.Integer", result.get(1).getRawClass().getName());
        } catch (IllegalArgumentException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFindClass() {
        try {
            Class<?> result = typeParser.findClass("java.util.List", typeParser.new MyTokenizer(""));
            Assert.assertNotNull(result);
            Assert.assertEquals("java.util.List", result.getName());
        } catch (IllegalArgumentException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }
}