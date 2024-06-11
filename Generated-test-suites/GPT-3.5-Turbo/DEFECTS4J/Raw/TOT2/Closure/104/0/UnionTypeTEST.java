import static org.junit.Assert.*;
import org.junit.Test;

public class UnionTypeTest {

    @Test
    public void testGetAlternates() {
        UnionType unionType = new UnionType(registry, ImmutableSet.of(new StringType(), new BooleanType()));
        assertEquals(2, unionType.getAlternates().size());
    }

    @Test
    public void testMatchesNumberContext() {
        UnionType unionType = new UnionType(registry, ImmutableSet.of(new NumberType(), new BooleanType()));
        assertTrue(unionType.matchesNumberContext());
    }

    @Test
    public void testMatchesStringContext() {
        UnionType unionType = new UnionType(registry, ImmutableSet.of(new StringType(), new ObjectType()));
        assertTrue(unionType.matchesStringContext());
    }

    @Test
    public void testMatchesObjectContext() {
        UnionType unionType = new UnionType(registry, ImmutableSet.of(new ObjectType(), new FunctionType()));
        assertTrue(unionType.matchesObjectContext());
    }

    @Test
    public void testFindPropertyType() {
        UnionType unionType = new UnionType(registry, ImmutableSet.of(new ObjectType(), new StringType()));
        assertEquals("String", unionType.findPropertyType("length").toString());
    }

    @Test
    public void testCanAssignTo() {
        UnionType unionType = new UnionType(registry, ImmutableSet.of(new ObjectType(), new StringType()));
        assertTrue(unionType.canAssignTo(new ObjectType()));
    }

    @Test
    public void testCanBeCalled() {
        UnionType unionType = new UnionType(registry, ImmutableSet.of(new FunctionType(), new ObjectType()));
        assertTrue(unionType.canBeCalled());
    }

    @Test
    public void testIsNullable() {
        UnionType unionType = new UnionType(registry, ImmutableSet.of(new ObjectType(), new NullType()));
        assertTrue(unionType.isNullable());
    }

    @Test
    public void testToString() {
        UnionType unionType = new UnionType(registry, ImmutableSet.of(new StringType(), new NumberType()));
        assertEquals("(Number|String)", unionType.toString());
    }

    @Test
    public void testEquals() {
        UnionType unionType1 = new UnionType(registry, ImmutableSet.of(new ObjectType(), new StringType()));
        UnionType unionType2 = new UnionType(registry, ImmutableSet.of(new ObjectType(), new StringType()));
        assertTrue(unionType1.equals(unionType2));
    }

}