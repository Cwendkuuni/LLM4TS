import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class TypeBindingsTest {

    @Test
    public void testCreate() {
        Class<?> erasedType = HashMap.class;
        JavaType typeArg1 = JavaType.forClass(String.class);
        JavaType typeArg2 = JavaType.forClass(Integer.class);

        TypeBindings typeBindings = TypeBindings.create(erasedType, typeArg1, typeArg2);

        JavaType foundType1 = typeBindings.getBoundType(0);
        JavaType foundType2 = typeBindings.getBoundType(1);

        assertEquals(typeArg1, foundType1);
        assertEquals(typeArg2, foundType2);
    }

    @Test
    public void testEmptyBindings() {
        TypeBindings typeBindings = TypeBindings.emptyBindings();

        assertEquals(0, typeBindings.size());
        assertEquals(true, typeBindings.isEmpty());
    }

    @Test
    public void testFindBoundType() {
        Class<?> erasedType = ArrayList.class;
        JavaType typeArg1 = JavaType.forClass(String.class);
        JavaType typeArg2 = JavaType.forClass(Double.class);

        TypeBindings typeBindings = TypeBindings.create(erasedType, typeArg1, typeArg2);

        JavaType foundType1 = typeBindings.findBoundType("E");
        JavaType foundType2 = typeBindings.findBoundType("F");

        assertEquals(typeArg1, foundType1);
        assertEquals(null, foundType2);
    }

    @Test
    public void testWithUnboundVariable() {
        String unboundVariable = "T";
        TypeBindings originalBindings = TypeBindings.createIfNeeded(List.class, JavaType.forClass(String.class));

        TypeBindings updatedBindings = originalBindings.withUnboundVariable(unboundVariable);

        assertEquals(1, updatedBindings.size());
        assertEquals(unboundVariable, updatedBindings.getBoundName(0));
    }
}