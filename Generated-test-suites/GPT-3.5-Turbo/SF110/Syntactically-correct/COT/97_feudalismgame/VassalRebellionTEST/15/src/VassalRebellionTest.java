import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class VassalRebellionTest {

    private VassalRebellion vassalRebellion;
    private Fiefdoms fiefdomObj;

    @Before
    public void setUp() {
        vassalRebellion = spy(new VassalRebellion());
        fiefdomObj = mock(Fiefdoms.class);
        when(fiefdomObj.getName()).thenReturn("TestFiefdom");
        Map.map.put("TestFiefdom", fiefdomObj);
    }

    @Test
    public void testPerform_whenCollectionIsEmpty_shouldDoNothing() {
        Collection col = new ArrayList<>();
        vassalRebellion.perform(col);
        verifyZeroInteractions(fiefdomObj);
    }

    @Test
    public void testPerform_whenFiefdomNameIsNull_shouldDoNothing() {
        Collection col = new ArrayList<>();
        col.add(null);
        vassalRebellion.perform(col);
        verifyZeroInteractions(fiefdomObj);
    }

    @Test
    public void testPerform_whenFiefdomNameDoesNotExistInMap_shouldDoNothing() {
        Collection col = new ArrayList<>();
        col.add("NonExistingFiefdom");
        vassalRebellion.perform(col);
        verifyZeroInteractions(fiefdomObj);
    }

    @Test
    public void testPerform_whenFiefdomObjIsNull_shouldDoNothing() {
        vassalRebellion.fiefdomObj = null;
        Collection col = new ArrayList<>();
        col.add("TestFiefdom");
        vassalRebellion.perform(col);
        verifyZeroInteractions(fiefdomObj);
    }

    @Test
    public void testPerform_whenLoyalVassalsSizeIsZero_shouldCreateRollEvent() {
        vassalRebellion.fiefdomObj.setLoyalVassals(new ArrayList<>());
        Collection col = new ArrayList<>();
        col.add("TestFiefdom");
        vassalRebellion.perform(col);
        verify(fiefdomObj).getName();
        verify(fiefdomObj, never()).setRebelliousVassals(anyInt(), anyBoolean());
        verify(fiefdomObj, never()).setKingdom(anyBoolean());
        verify(fiefdomObj, never()).getPersonalCastles();
        verify(fiefdomObj, never()).getFiefdomCastles();
        verify(fiefdomObj, never()).getValue();
        verifyZeroInteractions(Map.map);
    }

    @Test
    public void testPerform_whenLoyalVassalsSizeMinusValueIsGreaterThanOrEqualToZero_shouldSetRebelliousVassalsAndSetKingdom() {
        List loyalVassals = new ArrayList<>();
        loyalVassals.add("Vassal1");
        vassalRebellion.fiefdomObj.setLoyalVassals(loyalVassals);
        vassalRebellion.fiefdomObj.setValue(1);
        Collection col = new ArrayList<>();
        col.add("TestFiefdom");
        vassalRebellion.perform(col);
        verify(fiefdomObj).setRebelliousVassals(anyInt(), eq(true));
        verify(fiefdomObj).setKingdom(eq(false));
        verify(fiefdomObj, never()).getPersonalCastles();
        verify(fiefdomObj, never()).getFiefdomCastles();
        verifyZeroInteractions(Map.map);
    }

    @Test
    public void testPerform_whenPersonalCastlesIsZeroAndFiefdomCastlesIsZero_shouldSetRebelliousVassals() {
        List loyalVassals = new ArrayList<>();
        loyalVassals.add("Vassal1");
        vassalRebellion.fiefdomObj.setLoyalVassals(loyalVassals);
        vassalRebellion.fiefdomObj.setValue(10);
        when(fiefdomObj.getPersonalCastles()).thenReturn(0);
        when(fiefdomObj.getFiefdomCastles()).thenReturn(0);
        Collection col = new ArrayList<>();
        col.add("TestFiefdom");
        vassalRebellion.perform(col);
        verify(fiefdomObj).setRebelliousVassals(anyInt(), eq(true));
        verify(fiefdomObj, never()).setKingdom(anyBoolean());
        verify(fiefdomObj).getPersonalCastles();
        verify(fiefdomObj).getFiefdomCastles();
        verifyZeroInteractions(Map.map);
    }

    @Test
    public void testPerform_whenPersonalCastlesIsGreaterThanZeroAndFiefdomCastlesIsZero_shouldSetRebelliousVassals() {
        List loyalVassals = new ArrayList<>();
        loyalVassals.add("Vassal1");
        vassalRebellion.fiefdomObj.setLoyalVassals(loyalVassals);
        vassalRebellion.fiefdomObj.setValue(10);
        when(fiefdomObj.getPersonalCastles()).thenReturn(1);
        when(fiefdomObj.getFiefdomCastles()).thenReturn(0);
        Collection col = new ArrayList<>();
        col.add("TestFiefdom");
        vassalRebellion.perform(col);
        verify(fiefdomObj).setRebelliousVassals(anyInt(), eq(true));
        verify(fiefdomObj, never()).setKingdom(anyBoolean());
        verify(fiefdomObj).getPersonalCastles();
        verify(fiefdomObj).getFiefdomCastles();
        verifyZeroInteractions(Map.map);
    }

    @Test
    public void testPerform_whenPersonalCastlesIsZeroAndFiefdomCastlesIsGreaterThanZero_shouldSetRebelliousVassals() {
        List loyalVassals = new ArrayList<>();
        loyalVassals.add("Vassal1");
        vassalRebellion.fiefdomObj.setLoyalVassals(loyalVassals);
        vassalRebellion.fiefdomObj.setValue(10);
        when(fiefdomObj.getPersonalCastles()).thenReturn(0);
        when(fiefdomObj.getFiefdomCastles()).thenReturn(1);
        Collection col = new ArrayList<>();
        col.add("TestFiefdom");
        vassalRebellion.perform(col);
        verify(fiefdomObj).setRebelliousVassals(anyInt(), eq(true));
        verify(fiefdomObj, never()).setKingdom(anyBoolean());
        verify(fiefdomObj).getPersonalCastles();
        verify(fiefdomObj).getFiefdomCastles();
        verifyZeroInteractions(Map.map);
    }

    @Test
    public void testPerform_whenPersonalCastlesIsGreaterThanZeroAndFiefdomCastlesIsGreaterThanZeroAndLoyalVassalsSizeMinusCalculatedValueIsGreaterThanZero_shouldSetRebelliousVassals() {
        List loyalVassals = new ArrayList<>();
        loyalVassals.add("Vassal1");
        vassalRebellion.fiefdomObj.setLoyalVassals(loyalVassals);
        vassalRebellion.fiefdomObj.setValue(10);
        when(fiefdomObj.getPersonalCastles()).thenReturn(1);
        when(fiefdomObj.getFiefdomCastles()).thenReturn(1);
        Collection col = new ArrayList<>();
        col.add("TestFiefdom");
        vassalRebellion.perform(col);
        verify(fiefdomObj).setRebelliousVassals(anyInt(), eq(true));
        verify(fiefdomObj, never()).setKingdom(anyBoolean());
        verify(fiefdomObj).getPersonalCastles();
        verify(fiefdomObj).getFiefdomCastles();
        verifyZeroInteractions(Map.map);
    }

    @Test
    public void testPerform_whenPersonalCastlesIsGreaterThanZeroAndFiefdomCastlesIsGreaterThanZeroAndLoyalVassalsSizeMinusCalculatedValueIsLessThanOrEqualToZero_shouldSetRebelliousVassals() {
        List loyalVassals = new ArrayList<>();
        loyalVassals.add("Vassal1");
        vassalRebellion.fiefdomObj.setLoyalVassals(loyalVassals);
        vassalRebellion.fiefdomObj.setValue(5);
        when(fiefdomObj.getPersonalCastles()).thenReturn(1);
        when(fiefdomObj.getFiefdomCastles()).thenReturn(1);
        Collection col = new ArrayList<>();
        col.add("TestFiefdom");
        vassalRebellion.perform(col);
        verify(fiefdomObj, never()).setKingdom(anyBoolean());
        verify(fiefdomObj).getPersonalCastles();
        verify(fiefdomObj).getFiefdomCastles();
        verifyZeroInteractions(Map.map);
    }

    @Test
    public void testPerform_whenLoyalVassalsSizeMinusPersonalCastlesTimesTenPlusFiefdomCastlesTimesTwentyFiveIsGreaterThanLoyalVassalsSize_shouldSetRebelliousVassals() {
        List loyalVassals = new ArrayList<>();
        loyalVassals.add("Vassal1");
        vassalRebellion.fiefdomObj.setLoyalVassals(loyalVassals);
        vassalRebellion.fiefdomObj.setValue(5);
        when(fiefdomObj.getPersonalCastles()).thenReturn(2);
        when(fiefdomObj.getFiefdomCastles()).thenReturn(2);
        Collection col = new ArrayList<>();
        col.add("TestFiefdom");
        vassalRebellion.perform(col);
        verify(fiefdomObj).setRebelliousVassals(anyInt(), eq(true));
        verify(fiefdomObj, never()).setKingdom(anyBoolean());
        verify(fiefdomObj).getPersonalCastles();
        verify(fiefdomObj).getFiefdomCastles();
        verifyZeroInteractions(Map.map);
    }

    // Add more test cases for other methods if any
    
    // Test cases for constructor

    // Test cases for other public methods

    // Test cases for private/internal methods

}