
import org.junit.Before;
import org.junit.Test;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import static org.junit.Assert.*;

public class VassalRebellionTest {

    private VassalRebellion vassalRebellion;

    @Before
    public void setUp() {
        vassalRebellion = new VassalRebellion();
    }

    @Test
    public void testPerform() {
        // Test for non-empty loyal vassal list and enough loyalty value
        // arrange
        final Fiefdoms fiefdomObj = new Fiefdoms();
        String fiefdomName = "TestFiefdom";
        fiefdomObj.setName(fiefdomName);
        fiefdomObj.addLoyalVassal("Vassal1");
        fiefdomObj.addValue(1);
        Map.map.put(fiefdomObj.getName(), fiefdomObj);
        Collection<String> col = new ArrayList<String>();
        col.add(fiefdomName);

        // act
        vassalRebellion.perform(col);

        // assert
        assertTrue(fiefdomObj.isRebelliousVassals());
        assertFalse(fiefdomObj.isKingdom());

        // Test for non-empty loyal vassal list and not enough loyalty value
        // arrange
        final Fiefdoms fiefdomObj2 = new Fiefdoms();
        String fiefdomName2 = "TestFiefdom2";
        fiefdomObj2.setName(fiefdomName2);
        fiefdomObj2.addLoyalVassal("Vassal1");
        fiefdomObj2.addValue(-1);
        Map.map.put(fiefdomObj2.getName(), fiefdomObj2);
        Collection<String> col2 = new ArrayList<String>();
        col2.add(fiefdomName2);

        // act
        vassalRebellion.perform(col2);

        // assert
        assertTrue(fiefdomObj2.isRebelliousVassals());

        // Test for empty loyal vassal list
        // arrange
        final Fiefdoms fiefdomObj3 = new Fiefdoms();
        String fiefdomName3 = "TestFiefdom3";
        fiefdomObj3.setName(fiefdomName3);
        Map.map.put(fiefdomObj3.getName(), fiefdomObj3);
        Collection<String> col3 = new ArrayList<String>();
        col3.add(fiefdomName3);

        // act
        vassalRebellion.perform(col3);

        // no assert as it is dependent on another class

    }

}
