import org.junit.*;
import java.util.*;

public class VassalRebellionTest {

    private VassalRebellion vassalRebellion;

    @Before
    public void setup() {
        vassalRebellion = new VassalRebellion();
    }

    @Test
    public void testPerform_emptyCollection() {
        Collection<String> col = new ArrayList<>();
        vassalRebellion.perform(col);
        // Assert the expected behavior
    }

    @Test
    public void testPerform_singleFiefdom() {
        Collection<String> col = new ArrayList<>();
        col.add("Fiefdom1");
        vassalRebellion.perform(col);
        // Assert the expected behavior
    }

    @Test
    public void testPerform_multipleFiefdoms() {
        Collection<String> col = new ArrayList<>();
        col.add("Fiefdom1");
        col.add("Fiefdom2");
        vassalRebellion.perform(col);
        // Assert the expected behavior
    }
}

