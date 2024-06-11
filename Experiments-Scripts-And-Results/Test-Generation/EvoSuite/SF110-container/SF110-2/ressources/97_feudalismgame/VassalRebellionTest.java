import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

public class VassalRebellionTest {

	private VassalRebellion vassalRebellion;

	@Before
	public void setUp() throws Exception {
		vassalRebellion = new VassalRebellion();
	}

	@Test
	public void testPerformWithRebelliousVassals() {
		// Set up a collection with a fiefdom name that has rebellious vassals
		Collection<String> col = new ArrayList<>();
		col.add("rebelliousFiefdom");
		Map.map.put("rebelliousFiefdom", new Fiefdoms());
		Map.map.get("rebelliousFiefdom").setLoyalVassals(new ArrayList<Vassals>((Collection<? extends Vassals>) new Vassals()));

		// Call the perform method and check that the rebellious vassals are set and the fiefdom is no longer a kingdom
		vassalRebellion.perform(col);
		assertEquals(5, Map.map.get("rebelliousFiefdom").getRebelliousVassals());
		assertFalse(Map.map.get("rebelliousFiefdom").isKingdom());
	}

	@Test
	public void testPerformWithAllLoyalVassals() {
		// Set up a collection with a fiefdom name that has all loyal vassals
		Collection<String> col = new ArrayList<>();
		col.add("loyalFiefdom");
		Map.map.put("loyalFiefdom", new Fiefdoms());
		Map.map.get("loyalFiefdom").setLoyalVassals(new ArrayList<Vassals>((Collection<? extends Vassals>) new Vassals()));

		// Call the perform method and check that no vassals are set to rebellious and the fiefdom is still a kingdom
		vassalRebellion.perform(col);
		assertEquals(0, Map.map.get("loyalFiefdom").getRebelliousVassals());
		assertTrue(Map.map.get("loyalFiefdom").isKingdom());
	}

	@Test
	public void testPerformWithPersonalCastles() {
		// Set up a collection with a fiefdom name that has personal castles and some rebellious vassals
		Collection<String> col = new ArrayList<>();
		col.add("personalCastleFiefdom");
		Map.map.put("personalCastleFiefdom", new Fiefdoms());
		Map.map.get("personalCastleFiefdom").setLoyalVassals(new ArrayList<Vassals>((Collection<? extends Vassals>) new Vassals()));
		Map.map.get("personalCastleFiefdom").setPersonalCastles(2);

		// Call the perform method and check that the correct number of vassals are set to rebellious and the fiefdom is still a kingdom
		vassalRebellion.perform(col);
		assertEquals(10, Map.map.get("personalCastleFiefdom").getRebelliousVassals());
		assertTrue(Map.map.get("personalCastleFiefdom").isKingdom());
	}

}