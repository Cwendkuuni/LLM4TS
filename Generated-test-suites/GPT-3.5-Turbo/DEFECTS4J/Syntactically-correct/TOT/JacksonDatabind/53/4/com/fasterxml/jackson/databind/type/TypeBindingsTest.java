
import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;

public class TypeBindingsTest {

    @Test
    public void testFindBoundType_shouldReturnNull_whenNameIsNotPresent() {
        TypeBindings typeBindings = new TypeBindings(new String[]{"name1", "name2"}, new JavaType[]{}, null);
        Assert.assertNull(typeBindings.findBoundType("name3"));
    }

    @Test
    public void testFindBoundType_shouldReturnBoundType_whenNameIsPresent() {
        JavaType javaType = JavaType.builder().build();
        TypeBindings typeBindings = new TypeBindings(new String[]{"name1", "name2"}, new JavaType[]{javaType}, null);
        Assert.assertEquals(javaType, typeBindings.findBoundType("name1"));
    }

    @Test
    public void testFindBoundType_shouldReturnBoundType_whenNameIsPresentAndTypeIsResolvedRecursively() {
        JavaType unresolvedJavaType = JavaType.builder().build();
        JavaType resolvedJavaType = JavaType.builder().withSelfReferencedType(unresolvedJavaType).build();
        TypeBindings typeBindings = new TypeBindings(new String[]{"name1", "name2"}, new JavaType[]{resolvedJavaType}, null);
        Assert.assertEquals(unresolvedJavaType, typeBindings.findBoundType("name1"));
    }

    @Test
    public void testCreate_shouldCreateTypeBindings_whenTypesAreNullAndLengthIsZero() {
        TypeBindings typeBindings = TypeBindings.create(Map.class, null);
        Assert.assertEquals(typeBindings.size(), 0);
    }

    @Test
    public void testCreate_shouldCreateTypeBindings_whenTypesAreNullAndLengthIsOne() {
        TypeBindings typeBindings = TypeBindings.createIfNeeded(List.class, null);
        Assert.assertEquals(typeBindings.size(), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_shouldThrowIllegalArgumentException_whenNamesLengthAndTypesLengthMismatch() {
        TypeBindings.create(ArrayList.class, new JavaType[]{});
    }

    @Test
    public void testCreate_shouldCreateTypeBindings_whenVarsLengthIsZero() {
        TypeBindings typeBindings = TypeBindings.create(ArrayList.class, new JavaType[]{});
        Assert.assertEquals(typeBindings.size(), 0);
    }

    @Test
    public void testCreate_shouldCreateTypeBindings_whenVarsLengthIsOne() {
        TypeBindings typeBindings = TypeBindings.create(ArrayList.class, new JavaType[]{JavaType.builder().build()});
        Assert.assertEquals(typeBindings.size(), 1);
    }

    @Test
    public void testCreate_shouldCreateTypeBindings_whenVarsLengthIsTwo() {
        TypeBindings typeBindings = TypeBindings.create(ArrayList.class, new JavaType[]{JavaType.builder().build(), JavaType.builder().build()});
        Assert.assertEquals(typeBindings.size(), 2);
    }

}
