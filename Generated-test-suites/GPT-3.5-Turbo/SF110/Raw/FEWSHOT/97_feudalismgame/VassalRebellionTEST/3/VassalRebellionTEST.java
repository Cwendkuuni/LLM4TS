package src;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class VassalRebellionTest {

    private VassalRebellion vassalRebellion;

    @Before
    public void setup() {
        vassalRebellion = new VassalRebellion();
    }

    @Test
    public void testPerform_withLoyalVassalsAndEnoughRebelliousVassals() {
        Fiefdoms fiefdomObj = Mockito.mock(Fiefdoms.class);
        Collection<String> col = new ArrayList<>();
        col.add("fiefdom1");

        Mockito.when(Map.map.get(Mockito.anyString())).thenReturn(fiefdomObj);
        Mockito.when(fiefdomObj.getLoyalVassals()).thenReturn(new ArrayList<String>());
        Mockito.when(fiefdomObj.getValue()).thenReturn(10);

        vassalRebellion.perform(col);

        Mockito.verify(fiefdomObj).setRebelliousVassals(10, true);
        Mockito.verify(fiefdomObj).setKingdom(false);
    }

    @Test
    public void testPerform_withLoyalVassalsAndNotEnoughRebelliousVassals() {
        Fiefdoms fiefdomObj = Mockito.mock(Fiefdoms.class);
        Collection<String> col = new ArrayList<>();
        col.add("fiefdom1");

        Mockito.when(Map.map.get(Mockito.anyString())).thenReturn(fiefdomObj);
        Mockito.when(fiefdomObj.getLoyalVassals()).thenReturn(new ArrayList<String>());
        Mockito.when(fiefdomObj.getValue()).thenReturn(5);

        vassalRebellion.perform(col);

        Mockito.verify(fiefdomObj).setRebelliousVassals(5, true);
        Mockito.verify(fiefdomObj, Mockito.never()).setKingdom(false);
    }

    @Test
    public void testPerform_withPersonalCastlesAndFiefdomCastles() {
        Fiefdoms fiefdomObj = Mockito.mock(Fiefdoms.class);
        Collection<String> col = new ArrayList<>();
        col.add("fiefdom1");

        Mockito.when(Map.map.get(Mockito.anyString())).thenReturn(fiefdomObj);
        Mockito.when(fiefdomObj.getLoyalVassals()).thenReturn(new ArrayList<String>());
        Mockito.when(fiefdomObj.getValue()).thenReturn(0);
        Mockito.when(fiefdomObj.getPersonalCastles()).thenReturn(1);
        Mockito.when(fiefdomObj.getFiefdomCastles()).thenReturn(2);

        vassalRebellion.perform(col);

        Mockito.verify(fiefdomObj).setRebelliousVassals(35, true);
    }

    @Test
    public void testPerform_withNoLoyalVassals() {
        Fiefdoms fiefdomObj = Mockito.mock(Fiefdoms.class);
        Collection<String> col = new ArrayList<>();
        col.add("fiefdom1");

        Mockito.when(Map.map.get(Mockito.anyString())).thenReturn(fiefdomObj);
        Mockito.when(fiefdomObj.getLoyalVassals()).thenReturn(null);

        RollEvent rollEvent = Mockito.mock(RollEvent.class);
        Mockito.whenNew(RollEvent.class).withArguments(Mockito.anyString()).thenReturn(rollEvent);

        vassalRebellion.perform(col);

        Mockito.verify(rollEvent, Mockito.times(1)).RollEvent(Mockito.anyString());
    }
}