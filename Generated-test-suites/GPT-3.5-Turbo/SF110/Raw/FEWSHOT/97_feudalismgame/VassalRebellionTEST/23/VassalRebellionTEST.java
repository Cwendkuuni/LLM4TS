
package src;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class VassalRebellionTest {

    private VassalRebellion vassalRebellion;
    private Fiefdoms fiefdomObj;
    private Map map;

    @Before
    public void setup() {
        vassalRebellion = new VassalRebellion();
        fiefdomObj = mock(Fiefdoms.class);
        map = mock(Map.class);
    }

    @Test
    public void testPerformWithLoyalVassals() {
        Collection<String> col = new ArrayList<>();
        col.add("fiefdomName");

        when(fiefdomObj.getPersonalCastles()).thenReturn(1);
        when(fiefdomObj.getFiefdomCastles()).thenReturn(2);
        when(fiefdomObj.getLoyalVassals()).thenReturn(new ArrayList<>());
        when(fiefdomObj.getValue()).thenReturn(3);

        Map map = new HashMap();
        map.put("fiefdomName", fiefdomObj);

        when(map.get("fiefdomName")).thenReturn(fiefdomObj);

        vassalRebellion.perform(col);

        verify(fiefdomObj, times(1)).setRebelliousVassals(3, true);
        verify(fiefdomObj, times(1)).setKingdom(false);
        verify(fiefdomObj, never()).setRebelliousVassals(anyInt(), anyBoolean());
    }

    @Test
    public void testPerformWithoutLoyalVassals() {
        Collection<String> col = new ArrayList<>();
        col.add("fiefdomName");

        when(fiefdomObj.getLoyalVassals()).thenReturn(new ArrayList<>());

        Map map = new HashMap();
        map.put("fiefdomName", fiefdomObj);

        when(map.get("fiefdomName")).thenReturn(fiefdomObj);

        vassalRebellion.perform(col);

        verify(fiefdomObj, never()).setRebelliousVassals(anyInt(), anyBoolean());
        verify(fiefdomObj, never()).setKingdom(anyBoolean());
    }

    @Test
    public void testPerformWithRebelliousVassals() {
        Collection<String> col = new ArrayList<>();
        col.add("fiefdomName");

        List loyalVassals = Arrays.asList("vassal1", "vassal2", "vassal3");
        when(fiefdomObj.getLoyalVassals()).thenReturn(loyalVassals);

        when(fiefdomObj.getValue()).thenReturn(3);

        Map map = new HashMap();
        map.put("fiefdomName", fiefdomObj);

        when(map.get("fiefdomName")).thenReturn(fiefdomObj);

        vassalRebellion.perform(col);

        verify(fiefdomObj, times(1)).setRebelliousVassals(3, true);
        verify(fiefdomObj, never()).setKingdom(anyBoolean());
    }

    @Test
    public void testPerformWithVassalNumbers() {
        Collection<String> col = new ArrayList<>();
        col.add("fiefdomName");

        List loyalVassals = Arrays.asList("vassal1", "vassal2", "vassal3", "vassal4");
        when(fiefdomObj.getLoyalVassals()).thenReturn(loyalVassals);
        when(fiefdomObj.getPersonalCastles()).thenReturn(1);
        when(fiefdomObj.getFiefdomCastles()).thenReturn(2);
        when(fiefdomObj.getName()).thenReturn("fiefdomName");

        Map map = new HashMap();
        map.put("fiefdomName", fiefdomObj);

        when(map.get("fiefdomName")).thenReturn(fiefdomObj);

        vassalRebellion.perform(col);

        verify(fiefdomObj, times(1)).setRebelliousVassals(4, true);
        verify(fiefdomObj, never()).setKingdom(anyBoolean());
    }

    @Test
    public void testPerformWithRollEvent() {
        Collection<String> col = new ArrayList<>();
        col.add("fiefdomName");

        when(fiefdomObj.getLoyalVassals()).thenReturn(new ArrayList<>());
        when(fiefdomObj.getName()).thenReturn("fiefdomName");

        Map map = new HashMap();
        map.put("fiefdomName", fiefdomObj);

        when(map.get("fiefdomName")).thenReturn(fiefdomObj);

        vassalRebellion.perform(col);

        verify(fiefdomObj, never()).setRebelliousVassals(anyInt(), anyBoolean());
        verify(fiefdomObj, never()).setKingdom(anyBoolean());
        verify(fiefdomObj, times(1)).getName();
    }
}
