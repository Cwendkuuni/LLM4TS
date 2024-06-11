import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.NoObjectType;
import com.google.javascript.rhino.jstype.UnknownType;
import com.google.javascript.rhino.jstype.UnionType;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class UnionTypeTest {

    @Test
    public void testCreateUnionType() {
        Set<JSType> alternates = new HashSet<>();
        JSType type1 = new NoObjectType();
        JSType type2 = new UnknownType();
        alternates.add(type1);
        alternates.add(type2);

        UnionType unionType = new UnionType(alternates);
        assertEquals(alternates, unionType.getAlternates());
    }

    @Test
    public void testMatchesNumberContext() {
        Set<JSType> alternates = new HashSet<>();
        JSType type1 = new NoObjectType();
        JSType type2 = new UnknownType();
        alternates.add(type1);
        alternates.add(type2);

        UnionType unionType = new UnionType(alternates);
        assertEquals(false, unionType.matchesNumberContext());
    }

    @Test
    public void testMatchesStringContext() {
        Set<JSType> alternates = new HashSet<>();
        JSType type1 = new NoObjectType();
        JSType type2 = new UnknownType();
        alternates.add(type1);
        alternates.add(type2);

        UnionType unionType = new UnionType(alternates);
        assertEquals(false, unionType.matchesStringContext());
    }

    @Test
    public void testMatchesObjectContext() {
        Set<JSType> alternates = new HashSet<>();
        JSType type1 = new NoObjectType();
        JSType type2 = new UnknownType();
        alternates.add(type1);
        alternates.add(type2);

        UnionType unionType = new UnionType(alternates);
        assertEquals(false, unionType.matchesObjectContext());
    }

    // Add more test cases for other methods as needed

}