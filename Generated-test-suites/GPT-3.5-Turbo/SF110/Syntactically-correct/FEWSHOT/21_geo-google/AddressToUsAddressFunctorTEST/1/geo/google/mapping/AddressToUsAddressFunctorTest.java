package geo.google.mapping;

import org.junit.Test;
import static org.junit.Assert.*;

public class AddressToUsAddressFunctorTest {
    @Test
    public void testExecute() {
        // Sample data for testing
        GeoAddress addr = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        addr.setAddressDetails(addressDetails);
        AddressDetails.Country country = new AddressDetails.Country();
        addressDetails.setCountry(country);
        
        // Test when country is null
        GeoUsAddress result = AddressToUsAddressFunctor.getInstance().execute(addr);
        assertNull(result.getCountry());
        assertNull(result.getState());
        assertNull(result.getCounty());
        assertNull(result.getCity());
        assertNull(result.getPostalCode());
        assertNull(result.getAddressLine1());
        assertNull(result.getAddressLine2());
        assertNull(result.getCoordinate());
        assertEquals(0, result.getAccuracy());
        
        // Test when country is not null but countryNames is empty
        addressDetails.getCountry().getCountryNameCode().clear();
        result = AddressToUsAddressFunctor.getInstance().execute(addr);
        assertNull(result.getCountry());
        assertNull(result.getState());
        assertNull(result.getCounty());
        assertNull(result.getCity());
        assertNull(result.getPostalCode());
        assertNull(result.getAddressLine1());
        assertNull(result.getAddressLine2());
        assertNull(result.getCoordinate());
        assertEquals(0, result.getAccuracy());
        
        // Test when country is not null and countryNames is not empty
        AddressDetails.Country.CountryNameCode countryName = new AddressDetails.Country.CountryNameCode();
        countryName.setContent("United States");
        addressDetails.getCountry().getCountryNameCode().add(countryName);
        result = AddressToUsAddressFunctor.getInstance().execute(addr);
        assertEquals("United States", result.getCountry());
        assertNull(result.getState());
        assertNull(result.getCounty());
        assertNull(result.getCity());
        assertNull(result.getPostalCode());
        assertNull(result.getAddressLine1());
        assertNull(result.getAddressLine2());
        assertNull(result.getCoordinate());
        assertEquals(0, result.getAccuracy());
        
        // Add data to test remaining parts of the method
        AddressDetails.Country.AdministrativeArea adminArea = new AddressDetails.Country.AdministrativeArea();
        addressDetails.getCountry().setAdministrativeArea(adminArea);
        AdministrativeArea.AdministrativeAreaName areaName = new AdministrativeArea.AdministrativeAreaName();
        areaName.setContent("California");
        adminArea.getAdministrativeAreaName().add(areaName);
        AdministrativeArea.SubAdministrativeArea subAdminArea = new AdministrativeArea.SubAdministrativeArea();
        adminArea.setSubAdministrativeArea(subAdminArea);
        AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName subAreaName = new AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName();
        subAreaName.setContent("Los Angeles");
        subAdminArea.getSubAdministrativeAreaName().add(subAreaName);
        AdministrativeArea.Locality locality = new AdministrativeArea.Locality();
        subAdminArea.setLocality(locality);
        Locality.LocalityName localityName = new Locality.LocalityName();
        localityName.setContent("Beverly Hills");
        locality.getLocalityName().add(localityName);
        PostalCode postalCode = new PostalCode();
        PostalCode.PostalCodeNumber number = new PostalCode.PostalCodeNumber();
        number.setContent("90210");
        postalCode.getPostalCodeNumber().add(number);
        locality.setPostalCode(postalCode);
        Thoroughfare thoroughfare = new Thoroughfare();
        ThoroughfareNameType thoroughfareName = new ThoroughfareNameType();
        thoroughfareName.setContent("Rodeo Drive");
        thoroughfare.getThoroughfareName().add(thoroughfareName);
        ThoroughfareNameType thoroughfareName2 = new ThoroughfareNameType();
        thoroughfareName2.setContent("Wilshire Blvd");
        thoroughfare.getThoroughfareName().add(thoroughfareName2);
        locality.setThoroughfare(thoroughfare);
        
        // Test all fields are populated
        result = AddressToUsAddressFunctor.getInstance().execute(addr);
        assertEquals("United States", result.getCountry());
        assertEquals("California", result.getState());
        assertEquals("Los Angeles", result.getCounty());
        assertEquals("Beverly Hills", result.getCity());
        assertEquals("90210", result.getPostalCode());
        assertEquals("Rodeo Drive", result.getAddressLine1());
        assertEquals("Wilshire Blvd", result.getAddressLine2());
        assertNull(result.getCoordinate());
        assertEquals(0, result.getAccuracy());
    }
}