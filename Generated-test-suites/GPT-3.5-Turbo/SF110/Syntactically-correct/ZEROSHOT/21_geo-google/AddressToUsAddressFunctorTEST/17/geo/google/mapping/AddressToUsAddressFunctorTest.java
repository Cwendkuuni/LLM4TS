import static org.junit.Assert.*;
import org.junit.Test;
import geo.google.datamodel.*;
import org.apache.commons.collections.*;

public class AddressToUsAddressFunctorTest {
    
    @Test
    public void testExecute() {
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        
        // Test case 1: address with country, state, city, postal code, and address lines
        GeoAddress addr1 = new GeoAddress();
        AddressDetails.Country country1 = new AddressDetails.Country();
        AddressDetails.Country.CountryNameCode countryName1 = new AddressDetails.Country.CountryNameCode();
        countryName1.setContent("United States");
        country1.getCountryNameCode().add(countryName1);
        addr1.setAddressDetails(new AddressDetails());
        addr1.getAddressDetails().setCountry(country1);
        AdministrativeArea adminArea1 = new AdministrativeArea();
        AdministrativeArea.AdministrativeAreaName adminAreaName1 = new AdministrativeArea.AdministrativeAreaName();
        adminAreaName1.setContent("California");
        adminArea1.getAdministrativeAreaName().add(adminAreaName1);
        adminArea1.setLocality(new Locality());
        Locality.LocalityName localityName1 = new Locality.LocalityName();
        localityName1.setContent("Los Angeles");
        adminArea1.getLocality().getLocalityName().add(localityName1);
        adminArea1.getLocality().setPostalCode(new PostalCode());
        PostalCode.PostalCodeNumber postalCodeNumber1 = new PostalCode.PostalCodeNumber();
        postalCodeNumber1.setContent("90001");
        adminArea1.getLocality().getPostalCode().getPostalCodeNumber().add(postalCodeNumber1);
        ThoroughfareNameType thoroughfareNameType1 = new ThoroughfareNameType();
        thoroughfareNameType1.setContent("Main St");
        adminArea1.getLocality().getThoroughfare().getThoroughfareName().add(thoroughfareNameType1);
        addr1.getAddressDetails().getCountry().setAdministrativeArea(adminArea1);
        
        GeoUsAddress expected1 = new GeoUsAddress();
        expected1.setCountry("United States");
        expected1.setState("California");
        expected1.setCity("Los Angeles");
        expected1.setPostalCode("90001");
        expected1.setAddressLine1("Main St");
        
        GeoUsAddress result1 = functor.execute(addr1);
        
        assertEquals(expected1, result1);
        
        // Test case 2: address without country
        GeoAddress addr2 = new GeoAddress();
        addr2.setAddressDetails(new AddressDetails());
        
        GeoUsAddress expected2 = new GeoUsAddress();
        
        GeoUsAddress result2 = functor.execute(addr2);
        
        assertEquals(expected2, result2);
        
        // Add more test cases here ...
        
    }
}