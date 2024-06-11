
package src;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class VassalRebellionTest {
    private VassalRebellion vassalRebellion;
    private Fiefdoms fiefdomObj;
    private Map<String, Fiefdoms> map;

    @Before
    public void setUp() {
        vassalRebellion = new VassalRebellion();
        fiefdomObj = mock(Fiefdoms.class);
        map = new HashMap<>();
        map.put("Fiefdom", fiefdomObj);
        Map.map = map;
    }

    @Test
    public void testPerform_WithLoyalVassalsAndEnoughVassals() {
        Collection<String> col = Arrays.asList("Fiefdom");
        when(fiefdomObj.getName()).thenReturn("Fiefdom");
        when(fiefdomObj.getLoyalVassals()).thenReturn(Arrays.asList("Vassal1", "Vassal2"));
        when(fiefdomObj.getValue()).thenReturn(2);

        vassalRebellion.perform(col);

        verify(fiefdomObj).setRebelliousVassals(2, true);
        verify(fiefdomObj).setKingdom(false);
        verify(fiefdomObj, never()).setRebelliousVassals(anyInt(), anyBoolean());
        verify(fiefdomObj, never()).getPersonalCastles();
        verify(fiefdomObj, never()).getFiefdomCastles();
    }

    @Test
    public void testPerform_WithLoyalVassalsAndNotEnoughVassals() {
        Collection<String> col = Arrays.asList("Fiefdom");
        when(fiefdomObj.getName()).thenReturn("Fiefdom");
        when(fiefdomObj.getLoyalVassals()).thenReturn(Arrays.asList("Vassal1", "Vassal2"));
        when(fiefdomObj.getValue()).thenReturn(4);

        vassalRebellion.perform(col);

        verify(fiefdomObj).setRebelliousVassals(2, true);
        verify(fiefdomObj).setKingdom(true);
        verify(fiefdomObj, never()).setRebelliousVassals(anyInt(), anyBoolean());
        verify(fiefdomObj, never()).getPersonalCastles();
        verify(fiefdomObj, never()).getFiefdomCastles();
    }

    @Test
    public void testPerform_WithNoLoyalVassals() {
        Collection<String> col = Arrays.asList("Fiefdom");
        when(fiefdomObj.getName()).thenReturn("Fiefdom");
        when(fiefdomObj.getLoyalVassals()).thenReturn(Collections.emptyList());

        vassalRebellion.perform(col);

        verify(fiefdomObj, never()).setRebelliousVassals(anyInt(), anyBoolean());
        verify(fiefdomObj).getName();
        verify(fiefdomObj).getLoyalVassals();
        verify(fiefdomObj, never()).getValue();
        verify(fiefdomObj, never()).setKingdom(anyBoolean());
        verify(fiefdomObj, never()).getPersonalCastles();
        verify(fiefdomObj, never()).getFiefdomCastles();
        verify(fiefdomObj, never()).setRebelliousVassals(anyInt(), anyBoolean());
    }
}
