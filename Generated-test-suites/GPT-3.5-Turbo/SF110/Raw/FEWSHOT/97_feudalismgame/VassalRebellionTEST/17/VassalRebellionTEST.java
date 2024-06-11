import org.junit.Test;
import org.mockito.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class VassalRebellionTest {

    @Test
    public void testPerform() {
        // Setup
        VassalRebellion vassalRebellion = new VassalRebellion();
        Fiefdoms fiefdomObj = mock(Fiefdoms.class);
        Collection<String> col = mock(Collection.class);
        Iterator<String> colIter = mock(Iterator.class);
        
        when(col.iterator()).thenReturn(colIter);
        when(colIter.next()).thenReturn("fiefdomName");
        when(Map.map.get("fiefdomName")).thenReturn(fiefdomObj);
        when(fiefdomObj.getPersonalCastles()).thenReturn(1);
        when(fiefdomObj.getFiefdomCastles()).thenReturn(2);
        when(fiefdomObj.getLoyalVassals().size()).thenReturn(4);
        when(fiefdomObj.getValue()).thenReturn(3);

        // Exercise
        vassalRebellion.perform(col);

        // Verify
        verify(fiefdomObj).setRebelliousVassals(3, true);
        verify(fiefdomObj).setKingdom(false);
        verify(fiefdomObj).setRebelliousVassals(1, true);
        verify(fiefdomObj).setRebelliousVassals(4, true);
        verifyNew(RollEvent.class).withArguments("fiefdomName");
    }
}