import org.junit.Test;

public class TShortHashTest {

    @Test
    public void testContains() {
        TShortHash hash = new TShortHash();
        hash.insertKey((short) 1);
        assert(hash.contains((short) 1));
    }

    @Test
    public void testForEach() {
        TShortHash hash = new TShortHash();
        hash.insertKey((short) 1);

        TShortProcedure procedure = new TShortProcedure() {
            @Override
            public boolean execute(short value) {
                System.out.println(value);
                return true;
            }
        };

        assert(hash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        TShortHash hash = new TShortHash();
        hash.insertKey((short) 1);
        hash.removeAt(0);
        assert(!hash.contains((short) 1));
    }

    @Test
    public void testIndex() {
        TShortHash hash = new TShortHash();
        hash.insertKey((short) 1);
        assert(hash.index((short) 1) == 0);
    }

    @Test
    public void testInsertKey() {
        TShortHash hash = new TShortHash();
        assert(hash.insertKey((short) 1) == 0);
        assert(hash.contains((short) 1));
    }

}