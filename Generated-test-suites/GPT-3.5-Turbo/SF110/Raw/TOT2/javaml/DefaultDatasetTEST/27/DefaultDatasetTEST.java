import static org.junit.Assert.*;

import org.junit.Test;

public class DefaultDatasetTest {

    @Test
    public void testAddAll() {
        DefaultDataset dataset = new DefaultDataset();
        Instance inst1 = new Instance();
        Instance inst2 = new Instance();
        assertTrue(dataset.addAll(Arrays.asList(inst1, inst2)));
        assertEquals(2, dataset.size());
    }

    @Test
    public void testClear() {
        DefaultDataset dataset = new DefaultDataset();
        Instance inst1 = new Instance();
        dataset.add(inst1);
        dataset.clear();
        assertEquals(0, dataset.size());
    }

    // Add more test cases for other methods here

}