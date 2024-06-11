
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TypeBindingsTest {
    @Test
    public void testEmptyBindings(){
        TypeBindings typeBindings = TypeBindings.emptyBindings();
        assertTrue(typeBindings.isEmpty());
        assertEquals(0, typeBindings.size());
    }

    @Test
    public void testCreate(){
        Class<?> erasedType = String.class;
        JavaType[] types = new JavaType[]{};
        TypeBindings typeBindings = TypeBindings.create(erasedType, types);
        assertEquals(0, typeBindings.size());
    }

    @Test
    public void testCreateIfNeededWithTypes(){
        Class<?> erasedType = AbstractList.class;
        JavaType[] types = new JavaType[]{};
        TypeBindings typeBindings = TypeBindings.createIfNeeded(erasedType, types);
        assertTrue(typeBindings.isEmpty());
        assertEquals(0, typeBindings.size());
    }

    @Test
    public void testCreateIfNeededWithoutTypes(){
        Class<?> erasedType = AbstractList.class;
        JavaType typeArg1 = new JavaType(){
        };
        TypeBindings typeBindings = TypeBindings.createIfNeeded(erasedType, typeArg1);
        assertFalse(typeBindings.isEmpty());
        assertEquals(1, typeBindings.size());
    }

    @Test
    public void testWithUnboundVariable(){
        TypeBindings typeBindings = TypeBindings.emptyBindings();
        TypeBindings newTypeBindings = typeBindings.withUnboundVariable("variableName");
        assertFalse(newTypeBindings.isEmpty());
        assertEquals(1, newTypeBindings.size());
    }

    @Test
    public void testFindBoundType(){
        String name = "name";
        JavaType javaType1 = new JavaType(){};
        JavaType javaType2 = new JavaType(){};
        String[] names = new String[]{name};
        JavaType[] types = new JavaType[]{javaType1, javaType2};
        TypeBindings typeBindings = new TypeBindings(names, types, null);

        JavaType boundType = typeBindings.findBoundType(name);
        assertEquals(javaType1, boundType);
    }

    @Test
    public void testGetSize(){
        String[] names = new String[]{"name"};
        JavaType[] types = new JavaType[]{};
        TypeBindings typeBindings = new TypeBindings(names, types, null);

        assertEquals(1, typeBindings.size());
    }

    @Test
    public void testGetBoundName(){
        String name = "name";
        String[] names = new String[]{name};
        JavaType[] types = new JavaType[]{};
        TypeBindings typeBindings = new TypeBindings(names, types, null);

        String boundName = typeBindings.getBoundName(0);
        assertEquals(name, boundName);
    }

    @Test
    public void testGetBoundNameInvalidIndex(){
        String[] names = new String[]{};
        JavaType[] types = new JavaType[]{};
        TypeBindings typeBindings = new TypeBindings(names, types, null);

        String boundName = typeBindings.getBoundName(10);
        assertEquals(null, boundName);
    }

    @Test
    public void testGetBoundType(){
        JavaType javaType = new JavaType(){};
        String[] names = new String[]{"name"};
        JavaType[] types = new JavaType[]{javaType};
        TypeBindings typeBindings = new TypeBindings(names, types, null);

        JavaType boundType = typeBindings.getBoundType(0);
        assertEquals(javaType, boundType);
    }

    @Test
    public void testGetBoundTypeInvalidIndex(){
        String[] names = new String[]{};
        JavaType[] types = new JavaType[]{};
        TypeBindings typeBindings = new TypeBindings(names, types, null);

        JavaType boundType = typeBindings.getBoundType(-1);
        assertEquals(null, boundType);
    }
}
