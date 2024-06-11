package src;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class VassalRebellionTest {
    private VassalRebellion vassalRebellion;

    @Before
    public void setUp() {
        vassalRebellion = new VassalRebellion();
        Map.map = mock(Map.class);
    }

    @Test
    public void testPerform() {
        Collection<String> col = new ArrayList<>();
        col.add("fiefdom1");

        Fiefdoms fiefdomObj = mock(Fiefdoms.class);
        Iterator<String> colIter = mock(Iterator.class);
        when(col.iterator()).thenReturn(colIter);
        when(colIter.next()).thenReturn("fiefdom1");
        when(Map.map.get("fiefdom1")).thenReturn(fiefdomObj);
        when(fiefdomObj.getPersonalCastles()).thenReturn(10);
        when(fiefdomObj.getFiefdomCastles()).thenReturn(5);
        when(fiefdomObj.getLoyalVassals()).thenReturn(new ArrayList<>());
        when(fiefdomObj.getValue()).thenReturn(10);
        when(fiefdomObj.getName()).thenReturn("fiefdom1");

        vassalRebellion.perform(col);

        // Assertion statements here...
    }

}