
import static org.junit.Assert.*;
import org.junit.Test;
import geo.google.datamodel.*;
import oasis.names.tc.ciq.xsdschema.xal._2.*;
import java.util.*;

public class AddressToUsAddressFunctorTest {

    @Test
    public void testExecute() {
        // Create a sample GeoAddress object
        GeoAddress addr = new GeoAddress();
        AddressDetails details = new AddressDetails();
        addr.setAddressDetails(details);
        
        // Add a country with country name code
        AddressDetails.Country country = new AddressDetails.Country();
        details.setCountry(country);
        AddressDetails.Country.CountryNameCode countryNameCode = new AddressDetails.Country.CountryNameCode();
        countryNameCode.setContent("US");
        country.getCountryNameCode().add(countryNameCode);
        
        // Add an administrative area
        AdministrativeArea adminArea = new AdministrativeArea();
        country.setAdministrativeArea(adminArea);
        AdministrativeArea.AdministrativeAreaName adminAreaName = new AdministrativeArea.AdministrativeAreaName();
        adminAreaName.setContent("California");
        adminArea.getAdministrativeAreaName().add(adminAreaName);
        
        // Add a sub-administrative area
        AdministrativeArea.SubAdministrativeArea subAdminArea = new AdministrativeArea.SubAdministrativeArea();
        adminArea.setSubAdministrativeArea(subAdminArea);
        AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName subAdminAreaName = new AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName();
        subAdminAreaName.setContent("Santa Clara County");
        subAdminArea.getSubAdministrativeAreaName().add(subAdminAreaName);
        
        // Add a locality
        Locality locality = new Locality();
        subAdminArea.setLocality(locality);
        Locality.LocalityName localityName = new Locality.LocalityName();
        localityName.setContent("San Jose");
        locality.getLocalityName().add(localityName);
        
        // Add a postal code
        PostalCode postalCode = new PostalCode();
        locality.setPostalCode(postalCode);
        PostalCode.PostalCodeNumber postalCodeNumber = new PostalCode.PostalCodeNumber();
        postalCodeNumber.setContent("95110");
        postalCode.getPostalCodeNumber().add(postalCodeNumber);
        
        // Add a thoroughfare
        Thoroughfare thoroughfare = new Thoroughfare();
        locality.setThoroughfare(thoroughfare);
        Thoroughfare.ThoroughfareName thoroughfareName1 = new Thoroughfare.ThoroughfareName();
        thoroughfareName1.setContent("First Street");
        thoroughfare.getThoroughfareName().add(thoroughfareName1);
        Thoroughfare.ThoroughfareName thoroughfareName2 = new Thoroughfare.ThoroughfareName();
        thoroughfareName2.setContent("Second Street");
        thoroughfare.getThoroughfareName().add(thoroughfareName2);
        
        // Create an instance of AddressToUsAddressFunctor
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        
        // Call the execute method and check the returned GeoUsAddress object
        GeoUsAddress usAddress = functor.execute(addr);
        assertEquals("US", usAddress.getCountry());
        assertEquals("California", usAddress.getState());
        assertEquals("Santa Clara County", usAddress.getCounty());
        assertEquals("San Jose", usAddress.getCity());
        assertEquals("95110", usAddress.getPostalCode());
        assertEquals("First Street", usAddress.getAddressLine1());
        assertEquals("Second Street", usAddress.getAddressLine2());
    }
}
