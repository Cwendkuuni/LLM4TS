import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class VassalRebellionTest {
    private VassalRebellion vassalRebellion;
    private Fiefdoms fiefdomObj;

    @Before
    public void setUp() {
        vassalRebellion = new VassalRebellion();
        fiefdomObj = mock(Fiefdoms.class);
    }

    @Test
    public void testPerform_WhenLoyalVassalsPresentAndRebelliousCountIsMoreThanZero() {
        Collection<String> col = new ArrayList<>();
        col.add("fiefdom1");

        when(Map.map.get("fiefdom1")).thenReturn(fiefdomObj);
        when(fiefdomObj.getLoyalVassals()).thenReturn(new ArrayList<>());
        when(fiefdomObj.getValue()).thenReturn(0);
        when(fiefdomObj.getPersonalCastles()).thenReturn(2);
        when(fiefdomObj.getFiefdomCastles()).thenReturn(1);
        when(fiefdomObj.getName()).thenReturn("fiefdom1");

        vassalRebellion.perform(col);

        verify(fiefdomObj, times(1)).setRebelliousVassals(0, true);
        verify(fiefdomObj, times(1)).setKingdom(false);
    }

    @Test
    public void testPerform_WhenLoyalVassalsPresentAndRebelliousCountIsLessThanZero() {
        Collection<String> col = new ArrayList<>();
        col.add("fiefdom1");

        when(Map.map.get("fiefdom1")).thenReturn(fiefdomObj);
        when(fiefdomObj.getLoyalVassals()).thenReturn(new ArrayList<>());
        when(fiefdomObj.getValue()).thenReturn(1);
        when(fiefdomObj.getPersonalCastles()).thenReturn(0);
        when(fiefdomObj.getFiefdomCastles()).thenReturn(0);
        when(fiefdomObj.getName()).thenReturn("fiefdom1");

        vassalRebellion.perform(col);

        verify(fiefdomObj, times(1)).setRebelliousVassals(1, true);
        verify(fiefdomObj, times(1)).setKingdom(false);
    }

    @Test
    public void testPerform_WhenLoyalVassalsNotPresent() {
        Collection<String> col = new ArrayList<>();
        col.add("fiefdom1");

        when(Map.map.get("fiefdom1")).thenReturn(fiefdomObj);
        when(fiefdomObj.getLoyalVassals()).thenReturn(null);
        when(fiefdomObj.getName()).thenReturn("fiefdom1");

        new RollEvent(fiefdomObj.getName());

        verify(fiefdomObj, never()).setRebelliousVassals(anyInt(), anyBoolean());
        verify(fiefdomObj, never()).setKingdom(anyBoolean());
    }

    @Test
    public void testPerform_WhenPersonalCastlesAndFiefdomCastlesGreaterThanZeroAndRebelliousCountIsMoreThanZero() {
        Collection<String> col = new ArrayList<>();
        col.add("fiefdom1");

        when(Map.map.get("fiefdom1")).thenReturn(fiefdomObj);
        when(fiefdomObj.getLoyalVassals()).thenReturn(new ArrayList<>());
        when(fiefdomObj.getValue()).thenReturn(3);
        when(fiefdomObj.getPersonalCastles()).thenReturn(2);
        when(fiefdomObj.getFiefdomCastles()).thenReturn(1);
        when(fiefdomObj.getName()).thenReturn("fiefdom1");

        vassalRebellion.perform(col);

        verify(fiefdomObj, times(1)).setRebelliousVassals(1, true);
    }

    @Test
    public void testPerform_WhenPersonalCastlesAndFiefdomCastlesGreaterThanZeroAndRebelliousCountIsLessThanZero() {
        Collection<String> col = new ArrayList<>();
        col.add("fiefdom1");

        when(Map.map.get("fiefdom1")).thenReturn(fiefdomObj);
        when(fiefdomObj.getLoyalVassals()).thenReturn(new ArrayList<>());
        when(fiefdomObj.getValue()).thenReturn(2);
        when(fiefdomObj.getPersonalCastles()).thenReturn(2);
        when(fiefdomObj.getFiefdomCastles()).thenReturn(1);
        when(fiefdomObj.getName()).thenReturn("fiefdom1");

        vassalRebellion.perform(col);

        verify(fiefdomObj, times(1)).setRebelliousVassals(0, true);
    }

    @Test
    public void testPerform_WhenPersonalCastlesAndFiefdomCastlesAreZero() {
        Collection<String> col = new ArrayList<>();
        col.add("fiefdom1");

        when(Map.map.get("fiefdom1")).thenReturn(fiefdomObj);
        when(fiefdomObj.getLoyalVassals()).thenReturn(new ArrayList<>());
        when(fiefdomObj.getValue()).thenReturn(1);
        when(fiefdomObj.getPersonalCastles()).thenReturn(0);
        when(fiefdomObj.getFiefdomCastles()).thenReturn(0);
        when(fiefdomObj.getName()).thenReturn("fiefdom1");

        vassalRebellion.perform(col);

        verify(fiefdomObj, never()).setRebelliousVassals(anyInt(), anyBoolean());
    }
}