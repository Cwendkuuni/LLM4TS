import static org.junit.Assert.*;
import org.junit.Test;
import geo.google.datamodel.*;
import oasis.names.tc.ciq.xsdschema.xal._2.*;

public class AddressToUsAddressFunctorTest {
    
    @Test
    public void testExecute() {
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoAddress geoAddress = createGeoAddress();
        
        GeoUsAddress expected = createExpectedUsAddress();
        GeoUsAddress actual = functor.execute(geoAddress);
        
        assertEquals(expected.getCountry(), actual.getCountry());
        assertEquals(expected.getState(), actual.getState());
        assertEquals(expected.getCounty(), actual.getCounty());
        assertEquals(expected.getCity(), actual.getCity());
        assertEquals(expected.getPostalCode(), actual.getPostalCode());
        assertEquals(expected.getAddressLine1(), actual.getAddressLine1());
        assertEquals(expected.getAddressLine2(), actual.getAddressLine2());
        assertEquals(expected.getCoordinate(), actual.getCoordinate());
        assertEquals(expected.getAccuracy(), actual.getAccuracy());
    }
    
    // Helper method to create a sample GeoAddress object
    private GeoAddress createGeoAddress() {
        GeoAddress geoAddress = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        geoAddress.setAddressDetails(addressDetails);
        
        AddressDetails.Country country = new AddressDetails.Country();
        addressDetails.setCountry(country);
        
        AddressDetails.Country.CountryNameCode countryName = new AddressDetails.Country.CountryNameCode();
        countryName.setContent("USA");
        country.getCountryNameCode().add(countryName);
        
        AdministrativeArea adminArea = new AdministrativeArea();
        country.setAdministrativeArea(adminArea);
        
        AdministrativeArea.AdministrativeAreaName adminAreaName = new AdministrativeArea.AdministrativeAreaName();
        adminAreaName.setContent("California");
        adminArea.getAdministrativeAreaName().add(adminAreaName);
        
        Locality locality = new Locality();
        adminArea.setLocality(locality);
        
        Locality.LocalityName localName = new Locality.LocalityName();
        localName.setContent("Los Angeles");
        locality.getLocalityName().add(localName);
        
        PostalCode postalCode = new PostalCode();
        locality.setPostalCode(postalCode);
        
        PostalCode.PostalCodeNumber postalCodeNumber = new PostalCode.PostalCodeNumber();
        postalCodeNumber.setContent("90001");
        postalCode.getPostalCodeNumber().add(postalCodeNumber);
        
        Thoroughfare thoroughfare = new Thoroughfare();
        locality.setThoroughfare(thoroughfare);
        
        ThoroughfareNameType thoroughfareName1 = new ThoroughfareNameType();
        thoroughfareName1.setContent("123 Main St");
        thoroughfare.getThoroughfareName().add(thoroughfareName1);
        
        ThoroughfareNameType thoroughfareName2 = new ThoroughfareNameType();
        thoroughfareName2.setContent("Apt 4");
        thoroughfare.getThoroughfareName().add(thoroughfareName2);
        
        return geoAddress;
    }
    
    // Helper method to create the expected GeoUsAddress object
    private GeoUsAddress createExpectedUsAddress() {
        GeoUsAddress expected = new GeoUsAddress();
        expected.setCountry("USA");
        expected.setState("California");
        expected.setCity("Los Angeles");
        expected.setPostalCode("90001");
        expected.setAddressLine1("123 Main St");
        expected.setAddressLine2("Apt 4");
        
        return expected;
    }
}