
package src;

import org.junit.Test;

import java.util.Collection;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class VassalRebellionTest {

    @Test
    public void testPerform() {
        VassalRebellion vassalRebellion = new VassalRebellion();

        // Set up the test data
        Collection<String> col = mock(Collection.class);
        Iterator<String> colIter = mock(Iterator.class);
        Fiefdoms fiefdomObj = mock(Fiefdoms.class);

        // Mock the behavior of the dependencies
        when(col.iterator()).thenReturn(colIter);
        when(colIter.next()).thenReturn("TestFiefdom");
        when(Map.map.get("TestFiefdom")).thenReturn(fiefdomObj);
        when(fiefdomObj.getPersonalCastles()).thenReturn(10);
        when(fiefdomObj.getFiefdomCastles()).thenReturn(20);
        when(fiefdomObj.getLoyalVassals().size()).thenReturn(5);
        when(fiefdomObj.getValue()).thenReturn(3);

        // Perform the action
        vassalRebellion.perform(col);

        // Verify the expected behavior
        verify(fiefdomObj, times(1)).setRebelliousVassals(3, true);
        verify(fiefdomObj, times(1)).setKingdom(false);
        verify(fiefdomObj, times(1)).setRebelliousVassals(5, true);
        verify(fiefdomObj, times(1)).setRebelliousVassals(5 - (10 * 10 + 20 * 25), true);

        verify(cool, times(0)).getPersonalCastles();
        verify(cool, times(0)).getFiefdomCastles();
        verify(cool, times(0)).getName();
    }
}
