
import org.junit.Test;
import static org.junit.Assert.*;

public class FoldTest {

    @Test
    public void testAdd() {
        Fold fold = new Fold(new DefaultDataset(), new int[]{0, 1, 2});
        Instance instance = new Instance();
        
        assertThrows(UnsupportedOperationException.class, () -> {
            fold.add(instance);
        });
    }

    @Test
    public void testClasses() {
        Dataset parent = new DefaultDataset();
        Fold fold = new Fold(parent, new int[]{0, 1, 2});
        
        SortedSet<Object> classes = fold.classes();
        
        assertEquals(parent.classes(), classes);
    }

    @Test
    public void testInstance() {
        Dataset parent = new DefaultDataset();
        parent.add(new Instance());
        parent.add(new Instance());

        Fold fold = new Fold(parent, new int[]{1, 0});
        
        assertEquals(parent.instance(1), fold.instance(0));
        assertEquals(parent.instance(0), fold.instance(1));
    }
}
