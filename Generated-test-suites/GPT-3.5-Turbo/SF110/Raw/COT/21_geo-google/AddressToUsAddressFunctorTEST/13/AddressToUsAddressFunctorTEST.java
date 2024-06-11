
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import geo.google.mapping.AddressToUsAddressFunctor;
import geo.google.datamodel.GeoAddress;
import geo.google.datamodel.GeoUsAddress;

public class AddressToUsAddressFunctorTest {

    @Test
    public void testExecute() {
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoAddress addr = new GeoAddress(); // Create test GeoAddress object
        
        // Test case 1: Verify that it sets the country correctly
        // Edge case: Empty country names
        GeoUsAddress result1 = functor.execute(addr);
        assertEquals(null, result1.getCountry()); 
        
        // Edge case: Non-empty country names
        // Add non-empty country names to AddressDetails.Country
        addr.getAddressDetails().getCountry().addCountryNameCode("United States");
        GeoUsAddress result2 = functor.execute(addr);
        assertEquals("United States", result2.getCountry()); 
        
        // Test case 2: Verify that it sets the state correctly
        // Edge case: Empty names
        addr.getAddressDetails().getCountry().setAdministrativeArea(null);
        GeoUsAddress result3 = functor.execute(addr);
        assertEquals(null, result3.getState()); 
        
        // Edge case: Non-empty names
        // Add non-empty names to AdministrativeArea
        addr.getAddressDetails().getCountry().setAdministrativeArea(new AdministrativeArea());
        addr.getAddressDetails().getCountry().getAdministrativeArea().addAdministrativeAreaName("California");
        GeoUsAddress result4 = functor.execute(addr);
        assertEquals("California", result4.getState()); 
        
        // Test case 3: Verify that it sets the county correctly
        // Edge case: Empty names
        addr.getAddressDetails().getCountry().getAdministrativeArea().setSubAdministrativeArea(null);
        GeoUsAddress result5 = functor.execute(addr);
        assertEquals(null, result5.getCounty()); 
        
        // Edge case: Non-empty names
        // Add non-empty names to SubAdministrativeArea
        addr.getAddressDetails().getCountry().getAdministrativeArea().setSubAdministrativeArea(new AdministrativeArea.SubAdministrativeArea());
        addr.getAddressDetails().getCountry().getAdministrativeArea().getSubAdministrativeArea().addSubAdministrativeAreaName("Los Angeles");
        GeoUsAddress result6 = functor.execute(addr);
        assertEquals("Los Angeles", result6.getCounty()); 
        
        // Test case 4: Verify that it sets the city correctly
        // Edge case: Empty names
        addr.getAddressDetails().getCountry().getAdministrativeArea().setLocality(null);
        GeoUsAddress result7 = functor.execute(addr);
        assertEquals(null, result7.getCity()); 
        
        // Edge case: Non-empty names
        // Add non-empty names to Locality
        addr.getAddressDetails().getCountry().getAdministrativeArea().setLocality(new Locality());
        addr.getAddressDetails().getCountry().getAdministrativeArea().getLocality().addLocalityName("Los Angeles");
        GeoUsAddress result8 = functor.execute(addr);
        assertEquals("Los Angeles", result8.getCity());
        
        // Test case 5: Verify that it sets the postal code correctly
        // Edge case: Empty postal code number
        addr.getAddressDetails().getCountry().getAdministrativeArea().getLocality().setPostalCode(null);
        GeoUsAddress result9 = functor.execute(addr);
        assertEquals(null, result9.getPostalCode()); 
        
        // Edge case: Non-empty postal code number
        // Add non-empty postal code number to PostalCode
        addr.getAddressDetails().getCountry().getAdministrativeArea().getLocality().setPostalCode(new PostalCode());
        addr.getAddressDetails().getCountry().getAdministrativeArea().getLocality().getPostalCode().addPostalCodeNumber("90001");
        GeoUsAddress result10 = functor.execute(addr);
        assertEquals("90001", result10.getPostalCode());
        
        // Test case 6: Verify that it sets the address lines correctly
        // Edge case: Empty thoroughfare names
        addr.getAddressDetails().getCountry().getAdministrativeArea().getLocality().setThoroughfare(null);
        GeoUsAddress result11 = functor.execute(addr);
        assertEquals(null, result11.getAddressLine1());
        assertEquals(null, result11.getAddressLine2());
        
        // Edge case: Single thoroughfare name
        // Add a thoroughfare name to ThoroughfareNameType
        addr.getAddressDetails().getCountry().getAdministrativeArea().getLocality().setThoroughfare(new Thoroughfare());
        addr.getAddressDetails().getCountry().getAdministrativeArea().getLocality().getThoroughfare().addThoroughfareName("Main Street");
        GeoUsAddress result12 = functor.execute(addr);
        assertEquals("Main Street", result12.getAddressLine1());
        assertEquals(null, result12.getAddressLine2());
        
        // Edge case: Multiple thoroughfare names
        // Add multiple thoroughfare names to ThoroughfareNameType
        addr.getAddressDetails().getCountry().getAdministrativeArea().getLocality().getThoroughfare().addThoroughfareName("Building A");
        GeoUsAddress result13 = functor.execute(addr);
        assertEquals("Main Street", result13.getAddressLine1());
        assertEquals("Building A", result13.getAddressLine2());
        
        // Test case 7: Verify that it sets the coordinate correctly
        // Edge case: Null coordinate
        addr.setCoordinate(null);
        GeoUsAddress result14 = functor.execute(addr);
        assertEquals(null, result14.getCoordinate());
        
        // Edge case: Non-null coordinate
        // Add a non-null coordinate to GeoAddress
        addr.setCoordinate(new Coordinate());
        GeoUsAddress result15 = functor.execute(addr);
        assertEquals(new Coordinate(), result15.getCoordinate());
        
        // Test case 8: Verify that it sets the accuracy correctly
        // Edge case: Zero accuracy
        addr.setAccuracy(0);
        GeoUsAddress result16 = functor.execute(addr);
        assertEquals(0, result16.getAccuracy());
        
        // Edge case: Non-zero accuracy
        addr.setAccuracy(1);
        GeoUsAddress result17 = functor.execute(addr);
        assertEquals(1, result17.getAccuracy());
    }
}
