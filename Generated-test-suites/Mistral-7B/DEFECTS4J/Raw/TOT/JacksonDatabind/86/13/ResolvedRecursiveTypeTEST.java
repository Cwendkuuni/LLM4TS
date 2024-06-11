import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        Class<ResolvedRecursiveType> typeClass = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(typeClass, bindings);

        JavaType javaType = new SimpleType("com.example.TestType");
        type.setReference(javaType);

        JavaType expected = javaType;
        JavaType actual = type.getSelfReferencedType();

        assertEquals(expected, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        Class<ResolvedRecursiveType> typeClass = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(typeClass, bindings);

        JavaType javaType = new SimpleType("com.example.TestType");
        type.setReference(javaType);

        JavaType ref = new SimpleType("com.example.TestType2");
        type.setReference(ref);
    }

    @Test
    public void testGetGenericSignature() {
        Class<ResolvedRecursiveType> typeClass = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(typeClass, bindings);

        JavaType javaType = new SimpleType("com.example.TestType");
        type.setReference(javaType);

        StringBuilder sb = new StringBuilder();
        type.getGenericSignature(sb);

        String expected = "com.example.TestType<T extends " + type.getType().getErasedSignature(new StringBuilder()).toString() + ">";
        String actual = sb.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        Class<ResolvedRecursiveType> typeClass = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();

        ResolvedRecursiveType type1 = new ResolvedRecursiveType(typeClass, bindings);
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(typeClass, bindings);

        JavaType javaType = new SimpleType("com.example.TestType");
        type1.setReference(javaType);
        type2.setReference(javaType);

        assertTrue(type1.equals(type2));
    }

    @Test
    public void testNotEquals() {
        Class<ResolvedRecursiveType> typeClass = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();

        ResolvedRecursiveType type1 = new ResolvedRecursiveType(typeClass, bindings);
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(typeClass, bindings);

        JavaType javaType1 = new SimpleType("com.example.TestType");
        type1.setReference(javaType1);

        JavaType javaType2 = new SimpleType("com.example.TestType2");
        type2.setReference(javaType2);

        assertFalse(type1.equals(type2));
    }

    @Test
    public void testToString() {
        Class<ResolvedRecursiveType> typeClass = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();

        ResolvedRecursiveType type = new ResolvedRecursiveType(typeClass, bindings);
        JavaType javaType = new SimpleType("com.example.TestType");
        type.setReference(javaType);

        String expected = "[recursive type; com.example.TestType]";
        String actual = type.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testIsContainerType() {
        Class<ResolvedRecursiveType> typeClass = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();

        ResolvedRecursiveType type = new ResolvedRecursiveType(typeClass, bindings);

        assertFalse(type.isContainerType());
    }
}