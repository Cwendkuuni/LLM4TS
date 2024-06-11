import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertTrue(bindings.isEmpty());
    }

    @Test
    public void testCreateWithTypeList() {
        List<JavaType> typeList = new ArrayList<>(Arrays.asList(new JavaType(), new JavaType()));
        TypeBindings bindings = TypeBindings.create(String.class, typeList);
        assertFalse(bindings.isEmpty());
        assertEquals(2, bindings.size());
    }

    @Test
    public void testCreateWithTypeArray() {
        JavaType[] types = {new JavaType(), new JavaType()};
        TypeBindings bindings = TypeBindings.create(String.class, types);
        assertFalse(bindings.isEmpty());
        assertEquals(2, bindings.size());
    }

    @Test
    public void testFindBoundType() {
        JavaType type = new JavaType();
        JavaType[] types = {type};
        String[] names = {"type"};
        TypeBindings bindings = new TypeBindings(names, types, null);
        
        JavaType foundType = bindings.findBoundType("type");
        assertEquals(type, foundType);
    }

    @Test
    public void testWithUnboundVariable() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        TypeBindings updatedBindings = bindings.withUnboundVariable("variable");
        
        assertFalse(updatedBindings.isEmpty());
    }

    @Test
    public void testGetBoundType() {
        JavaType type = new JavaType();
        JavaType[] types = {type};
        String[] names = {"type"};
        TypeBindings bindings = new TypeBindings(names, types, null);
        
        JavaType boundType = bindings.getBoundType(0);
        assertEquals(type, boundType);
    }

    @Test
    public void testToString() {
        JavaType[] types = {new JavaType(), new JavaType()};
        String[] names = {"type1", "type2"};
        TypeBindings bindings = new TypeBindings(names, types, null);

        String expected = "<type1,type2>";
        assertEquals(expected, bindings.toString());
    }

}