import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings empty = TypeBindings.emptyBindings();
        assertEquals(0, empty.size());
        assertTrue(empty.isEmpty());
    }

    @Test
    public void testCreateWithNullTypes() {
        TypeBindings typeBindings = TypeBindings.create(TestClass.class, null);
        assertEquals(0, typeBindings.size());
        assertTrue(typeBindings.isEmpty());
    }

    @Test
    public void testCreateWithSingleType() {
        JavaType type = new JavaType();
        TypeBindings typeBindings = TypeBindings.create(TestClass.class, type);
        assertEquals(1, typeBindings.size());
        assertEquals(type, typeBindings.getBoundType(0));
    }

    @Test
    public void testCreateWithMultipleTypes() {
        JavaType type1 = new JavaType();
        JavaType type2 = new JavaType();
        TypeBindings typeBindings = TypeBindings.create(TestClass.class, type1, type2);
        assertEquals(2, typeBindings.size());
        assertEquals(type1, typeBindings.getBoundType(0));
        assertEquals(type2, typeBindings.getBoundType(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithMismatchingTypesAndNames() {
        JavaType type = new JavaType();
        TypeBindings.create(TestClass.class, new JavaType[] { type, type });
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithMismatchingTypesAndNames2() {
        JavaType type1 = new JavaType();
        JavaType type2 = new JavaType();
        JavaType type3 = new JavaType();
        TypeBindings.create(TestClass.class, type1, type2, type3);
    }

    @Test
    public void testCreateIfNeededWithNoTypeParameters() {
        JavaType type = new JavaType();
        TypeBindings typeBindings = TypeBindings.createIfNeeded(NoParamClass.class, type);
        assertEquals(0, typeBindings.size());
        assertTrue(typeBindings.isEmpty());
    }

    @Test
    public void testCreateIfNeededWithSingleTypeParameter() {
        JavaType type = new JavaType();
        TypeBindings typeBindings = TypeBindings.createIfNeeded(OneParamClass.class, type);
        assertEquals(1, typeBindings.size());
        assertEquals(type, typeBindings.getBoundType(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateIfNeededWithMultipleTypeParameters() {
        JavaType type1 = new JavaType();
        JavaType type2 = new JavaType();
        TypeBindings.createIfNeeded(MultipleParamClass.class, new JavaType[] { type1, type2 });
    }

    @Test
    public void testFindBoundType() {
        JavaType type1 = new JavaType();
        JavaType type2 = new JavaType();

        TypeBindings typeBindings = TypeBindings.create(TestClass.class, type1, type2);

        assertEquals(type1, typeBindings.findBoundType("T"));
        assertEquals(type2, typeBindings.findBoundType("E"));
    }

    @Test
    public void testWithUnboundVariable() {
        String unbound = "A";
        TypeBindings typeBindings = TypeBindings.emptyBindings().withUnboundVariable(unbound);
        assertTrue(typeBindings.hasUnbound(unbound));
    }

    @Test
    public void testTypeParameters() {
        JavaType type1 = new JavaType();
        JavaType type2 = new JavaType();

        TypeBindings typeBindings = TypeBindings.create(TestClass.class, type1, type2);

        List<JavaType> typeParameters = typeBindings.getTypeParameters();
        assertEquals(2, typeParameters.size());
        assertEquals(type1, typeParameters.get(0));
        assertEquals(type2, typeParameters.get(1));
    }
}