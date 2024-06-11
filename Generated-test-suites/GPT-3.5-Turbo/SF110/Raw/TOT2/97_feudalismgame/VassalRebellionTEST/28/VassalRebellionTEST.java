import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

public class VassalRebellionTest {

    @Test
    public void testPerform() {
        VassalRebellion vassalRebellion = new VassalRebellion();

        // Typical use case
        List<String> col = new ArrayList<>();
        col.add("TestFiefdom");
        vassalRebellion.perform(col);

        // Edge cases
        // Edge case when loyal vassals size is equal to value
        List<String> colEdgeCase1 = new ArrayList<>();
        colEdgeCase1.add("EdgeCaseFiefdom1");
        vassalRebellion.perform(colEdgeCase1);

        // Edge case when loyal vassals size is less than value and personalCastles/fiefdomCastles > 0
        List<String> colEdgeCase2 = new ArrayList<>();
        colEdgeCase2.add("EdgeCaseFiefdom2");
        vassalRebellion.perform(colEdgeCase2);

        // Edge case when loyal vassals size is 0
        List<String> colEdgeCase3 = new ArrayList<>();
        colEdgeCase3.add("EdgeCaseFiefdom3");
        vassalRebellion.perform(colEdgeCase3);

        // Error scenarios
        // Error scenario when collection is empty
        List<String> colErrorCase = new ArrayList<>();
        vassalRebellion.perform(colErrorCase);
    }
}