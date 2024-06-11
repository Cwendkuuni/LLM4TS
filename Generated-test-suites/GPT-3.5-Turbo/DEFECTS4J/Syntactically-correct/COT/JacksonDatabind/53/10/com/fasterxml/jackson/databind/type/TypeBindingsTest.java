
import org.junit.Test;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

public class TypeBindingsTest {

    @Test
    public void testFindBoundType() {
        TypeBindings typeBindings = TypeBindings.emptyBindings();
        JavaType boundType = typeBindings.findBoundType("name");
        assert(boundType == null);
    }

    @Test
    public void testIsEmpty() {
        TypeBindings typeBindings = TypeBindings.emptyBindings();
        boolean isEmpty = typeBindings.isEmpty();
        assert(isEmpty == true);
    }

    @Test
    public void testSize() {
        TypeBindings typeBindings = TypeBindings.emptyBindings();
        int size = typeBindings.size();
        assert(size == 0);
    }

    @Test
    public void testGetBoundName() {
        TypeBindings typeBindings = TypeBindings.emptyBindings();
        String boundName = typeBindings.getBoundName(0);
        assert(boundName == null);
    }

    @Test
    public void testGetBoundType() {
        TypeBindings typeBindings = TypeBindings.emptyBindings();
        JavaType boundType = typeBindings.getBoundType(0);
        assert(boundType == null);
    }

    @Test
    public void testGetTypeParameters() {
        TypeBindings typeBindings = TypeBindings.emptyBindings();
        List<JavaType> typeParameters = typeBindings.getTypeParameters();
        assert(typeParameters.size() == 0);
    }

    @Test
    public void testHasUnbound() {
        TypeBindings typeBindings = TypeBindings.emptyBindings();
        boolean hasUnbound = typeBindings.hasUnbound("name");
        assert(hasUnbound == false);
    }

    @Test
    public void testToString() {
        TypeBindings typeBindings = TypeBindings.emptyBindings();
        String toString = typeBindings.toString();
        assert(toString.equals("<>"));
    }

    @Test
    public void testHashCode() {
        TypeBindings typeBindings = TypeBindings.emptyBindings();
        int hashCode = typeBindings.hashCode();
        assert(hashCode != 0);
    }

    @Test
    public void testEquals() {
        TypeBindings typeBindings1 = TypeBindings.emptyBindings();
        TypeBindings typeBindings2 = TypeBindings.emptyBindings();
        boolean equal = typeBindings1.equals(typeBindings2);
        assert(equal == true);
    }

    @Test
    public void testTypeParameterArray() {
        TypeBindings typeBindings = TypeBindings.emptyBindings();
        JavaType[] typeParameters = typeBindings.typeParameterArray();
        assert(typeParameters.length == 0);
    }

    @Test
    public void testWithUnboundVariable() {
        TypeBindings typeBindings = TypeBindings.emptyBindings();
        TypeBindings withUnbound = typeBindings.withUnboundVariable("name");
        assert(withUnbound != null);
    }

    @Test
    public void testCreate() {
        JavaType[] types = new JavaType[1];
        TypeBindings typeBindings = TypeBindings.create(HashMap.class, types);
        assert(typeBindings != null);
    }

    @Test
    public void testCreateIfNeeded() {
        JavaType[] types = new JavaType[1];
        TypeBindings typeBindings = TypeBindings.createIfNeeded(HashMap.class, types);
        assert(typeBindings != null);
    }
}
