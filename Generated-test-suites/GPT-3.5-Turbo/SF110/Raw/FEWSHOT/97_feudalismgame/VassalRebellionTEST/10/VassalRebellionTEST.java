import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Collection;
import java.util.Iterator;
import org.junit.Before;
import org.junit.Test;

public class VassalRebellionTest {
    private VassalRebellion vassalRebellion;
    private Fiefdoms fiefdoms;
    private Map map;

    @Before
    public void setup() {
        vassalRebellion = new VassalRebellion();
        fiefdoms = mock(Fiefdoms.class);
        map = mock(Map.class);

        Map.map.put("fiefdomName", fiefdoms);
    }

    @Test
    public void testPerformWithLoyalVassals() {
        Collection collection = mock(Collection.class);
        Iterator iterator = mock(Iterator.class);
        List<String> loyalVassals = new ArrayList<>();
        loyalVassals.add("vassal1");
        loyalVassals.add("vassal2");

        when(collection.iterator()).thenReturn(iterator);
        when(iterator.next()).thenReturn("fiefdomName");
        when(fiefdoms.getLoyalVassals()).thenReturn(loyalVassals);
        when(fiefdoms.getValue()).thenReturn(10);
        when(fiefdoms.getPersonalCastles()).thenReturn(1);
        when(fiefdoms.getFiefdomCastles()).thenReturn(2);
        when(fiefdoms.getLoyalVassals().size()).thenReturn(12);

        vassalRebellion.perform(collection);

        verify(fiefdoms).setRebelliousVassals(10, true);
        verify(fiefdoms).setKingdom(false);
        verify(fiefdoms).setRebelliousVassals(2, true);
        verify(fiefdoms, times(2)).getLoyalVassals().size();
        verify(fiefdoms).getName();
        verifyZeroInteractions(map);
    }

    @Test
    public void testPerformWithoutLoyalVassals() {
        Collection collection = mock(Collection.class);
        Iterator iterator = mock(Iterator.class);
        List<String> loyalVassals = new ArrayList<>();

        when(collection.iterator()).thenReturn(iterator);
        when(iterator.next()).thenReturn("fiefdomName");
        when(fiefdoms.getLoyalVassals()).thenReturn(loyalVassals);

        vassalRebellion.perform(collection);

        verify(fiefdoms, never()).setRebelliousVassals(anyInt(), anyBoolean());
        verify(fiefdoms).getName();
        verify(map).put(anyString(), any(Fiefdoms.class));
    }
}