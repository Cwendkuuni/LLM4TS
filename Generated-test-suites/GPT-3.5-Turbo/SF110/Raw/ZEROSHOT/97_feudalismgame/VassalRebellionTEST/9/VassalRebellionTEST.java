import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class VassalRebellionTest {

    private VassalRebellion vassalRebellion;
    private Fiefdoms fiefdomsObj;

    @Before
    public void setUp() {
        vassalRebellion = new VassalRebellion();
        fiefdomsObj = new Fiefdoms();
    }

    @Test
    public void testPerform() {

        Collection<String> col = new ArrayList<>();
        col.add("fiefdom1");

        vassalRebellion.perform(col);

        // write assertions for the expected behavior of the perform method

    }
}